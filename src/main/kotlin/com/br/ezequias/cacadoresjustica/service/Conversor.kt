package com.br.ezequias.cacadoresjustica.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Conversor {

    fun getPagina() {
        var url = "http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=7J0001D040000&processo.foro=271&uuidCaptcha=sajcaptcha_279c1c241c254bb4a60825e4a39101c1"
        var doc: Document = Jsoup.connect(url).get()
    }

}