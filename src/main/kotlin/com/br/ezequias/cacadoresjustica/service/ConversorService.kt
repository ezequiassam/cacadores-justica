package com.br.ezequias.cacadoresjustica.service

import com.br.ezequias.cacadoresjustica.data.DadosProcesso
import com.br.ezequias.cacadoresjustica.data.Movimentacoes
import com.br.ezequias.cacadoresjustica.data.PartesProcesso
import com.br.ezequias.cacadoresjustica.data.Processo
import com.br.ezequias.cacadoresjustica.exeption.BusinessError
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.SocketException

@Service
class ConversorService {

    var mensagemDefaut = "Ocorreu um erro ao processar a solicitação. Tente novamente mais tarde."

    fun getProcesso(url: String): Processo {
        var doc = getPagina(url)
        val elements: Elements = doc.getElementsByClass("secaoFormBody")
        var dadosProcesso = getElementsDadosProcesso(elements)
        var partes = doc.getElementById("tableTodasPartes")
        var movimentacoes = doc.getElementById("tabelaTodasMovimentacoes")
        return transformProcesso(dadosProcesso, partes, movimentacoes)
    }

    private fun getPagina(url: String): Document {
        var doc: Element
        try {
            doc = Jsoup.connect(url).get()
        } catch (e: SocketException) {
            throw BusinessError(mapError("Não foi possivel conectar. Por favor tente novamente mais tarde.", HttpStatus.NO_CONTENT))
        } catch (t: Throwable) {
            throw BusinessError(mapError(mensagemDefaut, HttpStatus.BAD_REQUEST))
        }
        var mensagem = doc.getElementById("spwTabelaMensagem")?.text()
        if (!mensagem.isNullOrEmpty()) {
            throw BusinessError(mapError(mensagem!!.replace("Atenção", "").trim(), HttpStatus.NO_CONTENT))
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
        try {

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
                var requerent = p.toList()
                partes.add(PartesProcesso(
                        requerent[0].second as String,
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
        } catch (t: Throwable) {
            throw BusinessError(mapError(mensagemDefaut, HttpStatus.BAD_REQUEST))
        }
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
            } else if (value.contains("RepreLeg:")) {
                var repreLeg = value.split("RepreLeg:")
                value = repreLeg.get(0)
                var reprensentates = repreLeg.subList(1, repreLeg.size) as MutableList<String>
                list.add(mapOf(
                        key to value,
                        "representantes" to reprensentates))
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

    private fun mapError(mensagem: String, status: HttpStatus): Map<String, Any?> {
        return mapOf(
                "tipo" to "erro",
                "codigo" to status.value(),
                "mensagem" to mensagem)
    }

}