package com.example.controldegastosv2.external

data class Indicador(
    val codigo: String,
    val nombre: String,
    val unidad_medida: String,
    val fecha: String,
    val valor: Double
)

data class IndicadoresResponse(
    val uf: Indicador,
    val dolar: Indicador
)