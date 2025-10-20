package com.example.controldegastosv2.data

class GastoRepository(private val gastoDao: GastoDao) {
    fun obtenerGastos() = gastoDao.obtenerTodos()
    suspend fun insertarGasto(gasto: GastoEntity) = gastoDao.insertar(gasto)
}