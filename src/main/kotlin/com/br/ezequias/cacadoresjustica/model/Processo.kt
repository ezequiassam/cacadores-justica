package com.br.ezequias.cacadoresjustica.model

class Processo(partesProcesso: PartesProcesso, movimentacoes: List<Movimentacoes>, dadosProcesso: DadosProcesso)

class Movimentacoes(data: String, movimento: String)

class PartesProcesso(requerente: String, advogados: List<String>, representante: String)

class DadosProcesso(numero: String, classe: String, area: String, asunto: String, dataDistribuicao: String,
                    juiz: String, valorAcao: String)

