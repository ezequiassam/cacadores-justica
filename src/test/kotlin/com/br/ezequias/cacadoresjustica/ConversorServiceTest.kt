package com.br.ezequias.cacadoresjustica

import com.br.ezequias.cacadoresjustica.service.ConversorService
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConversorServiceTest {

    @Test
    fun getPaginaTestSP() {
        var process = ConversorService().getProcesso("http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=7J0001D040000")
        checkNotNull(process.dadosProcesso)
        assert(process.movimentacoes.isNotEmpty())
        assert(process.partesProcesso.isNotEmpty())
    }

    @Test
    fun getPaginaTestMG() {
        var process = ConversorService().getProcesso("http://www.tjms.jus.br/cpopg5/search.do?cbPesquisa=NUMPROC&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsultaNuUnificado=08219015120188120001")
        checkNotNull(process.dadosProcesso)
        assert(process.movimentacoes.isNotEmpty())
        assert(process.partesProcesso.isNotEmpty())
    }
}