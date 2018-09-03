package com.br.ezequias.cacadoresjustica

import com.br.ezequias.cacadoresjustica.controller.CacadoresJusticaController
import com.br.ezequias.cacadoresjustica.service.ConversorService
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CacadoresJusticaControllerTest {

    lateinit var controller: CacadoresJusticaController

    @Before
    fun inicio() {
        controller = CacadoresJusticaController(ConversorService())
        controller.urlTJSP = "http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=NUMEROPROCESSO"
        controller.urlTJSM = "http://www.tjms.jus.br/cpopg5/show.do?processo.codigo=NUMEROPROCESSO"
    }

    @Test
    fun carregarProcessoTJSPTest() {
        var map = mutableMapOf("numero" to "7J0001D040000", "tribunal" to "TJSP")
        var process = controller.carregarProcesso(map)
        assert(process.statusCode.is2xxSuccessful)
    }

    @Test
    fun carregarProcessoTJMGTest() {
        var map = mutableMapOf("numero" to "01001ZB2W0000", "tribunal" to "TJMJ")
        var process = controller.carregarProcesso(map)
        assert(process.statusCode.is2xxSuccessful)
    }
}