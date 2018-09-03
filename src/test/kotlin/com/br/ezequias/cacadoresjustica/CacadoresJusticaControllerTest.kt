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
        controller.urlTJSP = "http://esaj.tjsp.jus.br/cpopg/search.do?cbPesquisa=NUMPROC&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsultaNuUnificado=NUMEROPROCESSO"
        controller.urlTJSM = "http://www.tjms.jus.br/cpopg5/search.do?cbPesquisa=NUMPROC&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsultaNuUnificado=NUMEROPROCESSO"
    }

    @Test
    fun carregarProcessoTJSPTest() {
        var map = mutableMapOf("numero" to "10022988620158260271", "tribunal" to "TJSP")
        var process = controller.carregarProcesso(map)
        assert(process.statusCode.is2xxSuccessful)
    }

    @Test
    fun carregarProcessoTJMGTest() {
        var map = mutableMapOf("numero" to "08219015120188120001", "tribunal" to "TJMJ")
        var process = controller.carregarProcesso(map)
        assert(process.statusCode.is2xxSuccessful)
    }
}