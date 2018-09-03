package com.br.ezequias.cacadoresjustica.controller

import com.br.ezequias.cacadoresjustica.exeption.BusinessError
import com.br.ezequias.cacadoresjustica.service.ConversorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CacadoresJusticaController @Autowired constructor(val conversorService: ConversorService) {
    @Value("\${cacadores-justica.url.tjsp}")
    lateinit var urlTJSP: String

    @Value("\${cacadores-justica.url.tjsm}")
    lateinit var urlTJSM: String

    @RequestMapping("/processo", method = [RequestMethod.GET])
    fun carregarProcesso(@RequestParam params: Map<String, Any>): ResponseEntity<Any> {
        val numeroProcesso = params.getOrDefault("numero", "") as String
        val tribunal = params.getOrDefault("tribunal", "") as String
        if (StringUtils.isEmpty(numeroProcesso) || StringUtils.isEmpty(tribunal)) {
            return ResponseEntity.badRequest().build()
        }

        val headers = HttpHeaders()
        headers.cacheControl = CacheControl.noCache().headerValue
        val url = getUrl(numeroProcesso, tribunal)
        try {
            val processo = conversorService.getProcesso(url)
            return ResponseEntity(processo, headers, HttpStatus.OK)
        } catch (e: BusinessError) {
            return ResponseEntity(e.mensagem, headers, HttpStatus.valueOf(e.mensagem?.get("codigo").toString().toInt()))
        }
    }

    private fun getUrl(numeroProcesso: String, tribunal: String): String {
        if (tribunal.equals("TJSP", true)) {
            return urlTJSP.replace("NUMEROPROCESSO", numeroProcesso)
        } else {
            return urlTJSM.replace("NUMEROPROCESSO", numeroProcesso)
        }
    }
}