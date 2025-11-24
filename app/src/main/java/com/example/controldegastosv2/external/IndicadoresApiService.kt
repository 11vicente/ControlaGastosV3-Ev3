package com.example.controldegastosv2.external


import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface IndicadoresApiService {

    @GET("api")
    suspend fun obtenerIndicadores(): IndicadoresResponse

    companion object {
        val instance: IndicadoresApiService by lazy {
            Retrofit.Builder()
                .baseUrl("https://mindicador.cl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IndicadoresApiService::class.java)
        }
    }
}
