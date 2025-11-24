package com.example.controldegastosv2.ui.theme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controldegastosv2.data.Gasto
import com.example.controldegastosv2.data.GastoRepository
import kotlinx.coroutines.launch

class GastoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GastoRepository()

    private val _gastos = MutableLiveData<List<Gasto>>(emptyList())
    val gastos: LiveData<List<Gasto>> = _gastos

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
                    _gastos.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar gastos: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Error al cargar gastos: ${e.message}"
            }
        }
    }

    suspend fun agregarGastoConResultado(gasto: Gasto): Boolean {
        return try {
            val response = repository.crearGasto(gasto)

            if (response.isSuccessful) {

                val creado = response.body() ?: return false

                val listaActual = _gastos.value.orEmpty().toMutableList()
                listaActual.add(0, creado)
                _gastos.value = listaActual
                true
            } else {
                _error.value = "Error al agregar gasto: ${response.code()}"
                false
            }
        } catch (e: Exception) {
            _error.value = "Error al agregar gasto: ${e.message}"
            false
        }
    }

    fun actualizarGasto(gasto: Gasto) {
        val id = gasto.id ?: return
        viewModelScope.launch {
            try {
                val response = repository.actualizarGasto(id, gasto)

                if (response.isSuccessful) {
                    val actualizado = response.body() ?: return@launch
                    val lista = _gastos.value.orEmpty().toMutableList()

                    val index = lista.indexOfFirst { it.id == id }
                    if (index != -1) {
                        lista[index] = actualizado
                        _gastos.value = lista
                    }
                } else {
                    _error.value = "Error al actualizar gasto: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Error al actualizar gasto: ${e.message}"
            }
        }
    }

    fun eliminarGasto(id: Long) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarGasto(id)

                if (response.isSuccessful) {
                    _gastos.value = _gastos.value.orEmpty()
                        .filterNot { it.id == id }
                } else {
                    _error.value = "Error al eliminar gasto: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Error al eliminar gasto: ${e.message}"
            }
        }
    }
}
