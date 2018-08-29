package com.br.ezequias.cacadoresjustica

import com.br.ezequias.cacadoresjustica.service.Conversor
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConversorTest {

    @Test
    fun getPaginaTest() {
        var t = Conversor()
        t.getPagina()
    }
}