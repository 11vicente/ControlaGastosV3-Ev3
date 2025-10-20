package com.example.controldegastosv2.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GastoDao {
    @Insert
    suspend fun insertar(gasto: GastoEntity)

    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun obtenerTodos(): LiveData<List<GastoEntity>>

    @Query("DELETE FROM gastos")
    suspend fun borrarTodos()
}