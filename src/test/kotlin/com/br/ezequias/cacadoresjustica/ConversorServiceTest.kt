package com.br.ezequias.cacadoresjustica

import com.br.ezequias.cacadoresjustica.service.ConversorService
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConversorServiceTest {

    @Test
    fun getPaginaTest() {
        var process = ConversorService().getProcesso("http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=7J0001D040000")
        checkNotNull(process)
    }
}