package com.example.controldegastosv2.network

import com.example.controldegastosv2.data.Gasto
import retrofit2.Response
import retrofit2.http.*

interface GastoApiService {

    @GET("gastos")
    suspend fun getGastos(): Response<List<Gasto>>

    @POST("gastos")
    suspend fun crearGasto(@Body gasto: Gasto): Response<Gasto>

    @PUT("gastos/{id}")
    suspend fun actualizarGasto(
        @Path("id") id: Long,
        @Body gasto: Gasto
    ): Response<Gasto>

    @DELETE("gastos/{id}")
    suspend fun eliminarGasto(@Path("id") id: Long): Response<Void>
}
