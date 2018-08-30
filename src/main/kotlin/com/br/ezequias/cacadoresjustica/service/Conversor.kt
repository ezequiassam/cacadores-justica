package com.br.ezequias.cacadoresjustica.service

import com.br.ezequias.cacadoresjustica.model.Processo
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class Conversor {

//    TODO CRIAR TRATAMENTO PARA FALHA DE CONECÇÃO E PROCESSO NÃO ENCONTRADO
    fun getPagina() {
        var url = "http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=7J0001D040000&processo.foro=271&uuidCaptcha=sajcaptcha_279c1c241c254bb4a60825e4a39101c1"
        var doc = Jsoup.connect(url).get()
        var elements: Elements = doc.getElementsByClass("secaoFormBody")
        var dadosProcesso = getElementsDadosProcesso(elements)
        var partes = doc.getElementById("tableTodasPartes")
        var movimentacoes = doc.getElementById("tabelaTodasMovimentacoes")
        println("Dados do processo: " + dadosProcesso?.text() + "\nPartes envolvidas: " + partes.text() + "\nMovimentacoes: " + movimentacoes.text())
        transformProcesso(dadosProcesso, partes, movimentacoes)
    }

    fun getElementsDadosProcesso(elements: Elements): Element? {
        for (e in elements) {
            if (e.id().isEmpty()) {
                return e
            }
        }
        return null
    }

    fun transformProcesso(dadosProcesso: Element?, partes: Element, movimentacoes: Element) {
        var d = dadosProcesso?.children()?.get(0)?.children()
        var map = mutableMapOf<String, String>()
        for (t in d!!) {
            map.put(t.text().substringBefore(":").trim().toUpperCase(), t.text().substringAfter(":").trim())
        }
        var p = partes.dataset()
        var m = movimentacoes.dataset()
    }

}