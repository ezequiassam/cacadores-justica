package com.br.ezequias.cacadoresjustica.service

import com.br.ezequias.cacadoresjustica.model.DadosProcesso
import com.br.ezequias.cacadoresjustica.model.Movimentacoes
import com.br.ezequias.cacadoresjustica.model.PartesProcesso
import com.br.ezequias.cacadoresjustica.model.Processo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.SocketException

class Conversor {

    fun getProcesso(url: String): Processo {
        var doc = getPagina(url)
        val elements: Elements = doc.getElementsByClass("secaoFormBody")
        var dadosProcesso = getElementsDadosProcesso(elements)
        var partes = doc.getElementById("tableTodasPartes")
        var movimentacoes = doc.getElementById("tabelaTodasMovimentacoes")
        return transformProcesso(dadosProcesso, partes, movimentacoes)
    }
    
    //    TODO CRIAR TRATAMENTO PARA FALHA DE CONEXÃO E PROCESSO NÃO ENCONTRADO
    private fun getPagina(url: String): Document {
        var doc: Element
        try {
            doc = Jsoup.connect(url).get()
        } catch (e: SocketException) {
            throw e
        } catch (t: Throwable) {
            throw t
        }
        return doc
    }

    private fun getElementsDadosProcesso(elements: Elements): Element? {
        for (e in elements) {
            if (e.id().isEmpty()) {
                return e
            }
        }
        return null
    }

    private fun transformProcesso(dadosProcesso: Element?, partesProcesso: Element, movimentacoesProcesso: Element): Processo {
        var dadosMap = mapDados(dadosProcesso?.children()?.get(0)?.children())
        var partesList = listPartes(partesProcesso.children().get(0).children())
        var movimentacoesList = listMovimentacoes(movimentacoesProcesso.children())

        val dados = DadosProcesso(
                dadosMap.getOrDefault("processo", ""),
                dadosMap.getOrDefault("classe", ""),
                dadosMap.getOrDefault("área", ""),
                dadosMap.getOrDefault("assunto", ""),
                dadosMap.getOrDefault("distribuição", ""),
                dadosMap.getOrDefault("juiz", ""),
                dadosMap.getOrDefault("valor da ação", ""))

        val partes = mutableListOf<PartesProcesso>()
        for (p in partesList) {
            partes.add(PartesProcesso(
                    p.getOrDefault("reqte", "") as String,
                    p.getOrDefault("advogados", emptyList<String>()) as List<String>,
                    p.getOrDefault("representantes", emptyList<String>()) as List<String>
            ))
        }

        val movimentacoes = mutableListOf<Movimentacoes>()
        for (m in movimentacoesList) {
            movimentacoes.add(Movimentacoes(
                    m.getOrDefault("data", ""),
                    m.getOrDefault("movimento", "")
            ))
        }

        return Processo(partes, movimentacoes, dados)
    }

    private fun mapDados(elements: Elements?): MutableMap<String, String> {
        var map = mutableMapOf<String, String>()
        for (e in elements!!) {
            var key = e.text().substringBefore(":").trim().toLowerCase()
            var value = e.text().substringAfter(":").trim()
            map.put(key, value)
        }
        return map
    }

    private fun listPartes(elements: Elements?): List<Map<String, Any>> {
        var list = mutableListOf<Map<String, Any>>()
        for (e in elements!!) {
            var key = e.text().substringBefore(":").trim().toLowerCase()
            var value = e.text().substringAfter(":").trim()
            if (value.contains("Advogad(\\w):".toRegex())) {
                var advog = value.split("Advogad(\\w):".toRegex())
                value = advog.get(0)
                var advogados = advog.subList(1, advog.size) as MutableList<String>
                var reprensentates = mutableListOf<String>()
                for ((i, s) in advogados.withIndex()) {
                    if (s.contains("Reprtate:")) {
                        var represt = s.split("Reprtate:")
                        advogados[i] = represt.get(0).trim()
                        reprensentates.addAll(represt.subList(1, represt.size))
                    }
                }

                list.add(mapOf(
                        key to value,
                        "advogados" to advogados,
                        "representantes" to reprensentates
                ))
            } else {
                list.add(mapOf(key to value))
            }
        }
        return list
    }

    private fun listMovimentacoes(elements: Elements?): List<Map<String, String>> {
        var list = mutableListOf<Map<String, String>>()
        for (e in elements!!) {
            var key = e.text().trim().substring(0, 10).toLowerCase()
            var value = e.text().substring(11).trim()
            list.add(mapOf("data" to key, "movimento" to value))
        }
        return list
    }


}