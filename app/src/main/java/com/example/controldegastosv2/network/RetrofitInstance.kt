package com.example.controldegastosv2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://3.239.9.101:8080/api/"
    // EJEMPLO:
    // private const val BASE_URL = "http://192.168.1.20:8080/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GastoApiService by lazy {
        retrofit.create(GastoApiService::class.java)
    }
}
