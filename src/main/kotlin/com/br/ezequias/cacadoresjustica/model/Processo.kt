package com.br.ezequias.cacadoresjustica.model

class Processo(val partesProcesso: List<PartesProcesso>, val movimentacoes: List<Movimentacoes>, val dadosProcesso: DadosProcesso)

class Movimentacoes(val data: String, val movimento: String)

class PartesProcesso(val requerente: String, val advogados: List<String>, val representantes: List<String>)

class DadosProcesso(val numero: String, val classe: String, val area: String, val assunto: String, val dataDistribuicao: String,
                    val juiz: String, val valorAcao: String)

