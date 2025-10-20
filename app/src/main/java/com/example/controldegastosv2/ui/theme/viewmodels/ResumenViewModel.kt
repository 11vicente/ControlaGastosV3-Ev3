package com.example.controldegastosv2.ui.theme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.controldegastosv2.data.AppDatabase
import com.example.controldegastosv2.data.GastoEntity
import com.example.controldegastosv2.data.GastoRepository


class ResumenViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GastoRepository

    val todosLosGastos: LiveData<List<GastoEntity>>
    val totalGastado: LiveData<Double>
    val ultimoGasto: LiveData<GastoEntity?>
    val gastoMaximo: LiveData<GastoEntity?>

    init {
        val gastoDao = AppDatabase.getDatabase(application).gastoDao()
        repository = GastoRepository(gastoDao)
        todosLosGastos = repository.obtenerGastos()

        totalGastado = todosLosGastos.map { gastos ->
            gastos.sumOf { it.monto }
        }
        ultimoGasto = todosLosGastos.map { gastos ->
            gastos.maxByOrNull { it.fecha }
        }
        gastoMaximo = todosLosGastos.map { gastos ->
            gastos.maxByOrNull { it.monto }
        }
    }
}