package com.example.controldegastosv2.data

import com.example.controldegastosv2.network.RetrofitInstance
import com.example.controldegastosv2.network.GastoApiService

class GastoRepository {

    private val api = RetrofitInstance.api

    suspend fun obtenerGastos() = api.getGastos()

    suspend fun crearGasto(gasto: Gasto) = api.crearGasto(gasto)

    suspend fun actualizarGasto(id: Long, gasto: Gasto) =
        api.actualizarGasto(id, gasto)

    suspend fun eliminarGasto(id: Long) = api.eliminarGasto(id)
}
