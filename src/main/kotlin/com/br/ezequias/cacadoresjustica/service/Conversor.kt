package com.br.ezequias.cacadoresjustica.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class Conversor {

    //    TODO CRIAR TRATAMENTO PARA FALHA DE CONEXÃO E PROCESSO NÃO ENCONTRADO
    fun getPagina() {
        var url = "http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=7J0001D040000&processo.foro=271&uuidCaptcha=sajcaptcha_279c1c241c254bb4a60825e4a39101c1"
        var doc = Jsoup.connect(url).get()
        var elements: Elements = doc.getElementsByClass("secaoFormBody")
        var dadosProcesso = getElementsDadosProcesso(elements)
        var partes = doc.getElementById("tableTodasPartes")
        var movimentacoes = doc.getElementById("tabelaTodasMovimentacoes")
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
        var dadosMap = mapDados(dadosProcesso?.children()?.get(0)?.children())
        var partesList = listPartes(partes.children().get(0).children())
        var movimentacoesList = listMovimentacoes(movimentacoes.children())
    }

    fun mapDados(elements: Elements?): MutableMap<String, String> {
        var map = mutableMapOf<String, String>()
        for (e in elements!!) {
            var key = e.text().substringBefore(":").trim().toUpperCase()
            var value = e.text().substringAfter(":").trim()
            map.put(key, value)
        }
        return map
    }

    fun listPartes(elements: Elements?): List<Map<String, String>> {
        var list = mutableListOf<Map<String, String>>()
        for (e in elements!!) {
            var key = e.text().substringBefore(":").trim().toUpperCase()
            var value = e.text().substringAfter(":").trim()
            list.add(mapOf(key to value))
        }
        return list
    }

    fun listMovimentacoes(elements: Elements?): List<Map<String, String>> {
        var list = mutableListOf<Map<String, String>>()
        for (e in elements!!) {
            var key = e.text().trim().substring(0, 10).toUpperCase()
            var value = e.text().substring(11).trim()
            list.add(mapOf(key to value))
        }
        return list
    }


}