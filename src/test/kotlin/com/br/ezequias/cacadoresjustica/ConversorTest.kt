package com.br.ezequias.cacadoresjustica

import com.br.ezequias.cacadoresjustica.service.Conversor
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConversorTest {

    @Test
    fun getPaginaTest() {
        var t = Conversor()
        t.getProcesso("http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=7J0001D040000&processo.foro=271&uuidCaptcha=sajcaptcha_279c1c241c254bb4a60825e4a39101c1")
    }
}