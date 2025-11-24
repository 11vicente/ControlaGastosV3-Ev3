package com.example.controldegastosv2.ui.theme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.controldegastosv2.data.Gasto
import com.example.controldegastosv2.data.GastoRepository
import kotlinx.coroutines.launch

class ResumenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GastoRepository()

    private val _todosLosGastos = MutableLiveData<List<Gasto>>(emptyList())
    val todosLosGastos: LiveData<List<Gasto>> = _todosLosGastos

    val totalGastado: LiveData<Double> = todosLosGastos.map { gastos ->
        gastos.sumOf { it.monto }
    }

    val ultimoGasto: LiveData<Gasto?> = todosLosGastos.map { gastos ->
        gastos.maxByOrNull { it.fecha }
    }

    val gastoMaximo: LiveData<Gasto?> = todosLosGastos.map { gastos ->
        gastos.maxByOrNull { it.monto }
    }

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        cargarGastos()
    }

    fun cargarGastos() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerGastos()

                if (response.isSuccessful) {
                    _todosLosGastos.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar gastos: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Error al cargar gastos: ${e.message}"
            }
        }
    }
}
