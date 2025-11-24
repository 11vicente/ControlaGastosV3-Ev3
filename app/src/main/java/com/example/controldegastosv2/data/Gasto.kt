package com.example.controldegastosv2.data

data class Gasto(
    val id: Long? = null,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val fecha: Long,
    val fotoUri: String? = null
)
