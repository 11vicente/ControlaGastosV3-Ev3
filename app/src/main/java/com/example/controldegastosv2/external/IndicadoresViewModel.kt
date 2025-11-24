package com.example.controldegastosv2.external
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class IndicadoresViewModel : ViewModel() {

    private val _dolar = mutableStateOf<Double?>(null)
    val dolar: State<Double?> = _dolar

    private val _uf = mutableStateOf<Double?>(null)
    val uf: State<Double?> = _uf

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        cargarIndicadores()
    }

    fun cargarIndicadores() {
        viewModelScope.launch {
            try {
                val response = IndicadoresApiService.instance.obtenerIndicadores()
                _dolar.value = response.dolar.valor
                _uf.value = response.uf.valor
            } catch (e: Exception) {
                _error.value = "Error al cargar indicadores: ${e.message}"
            }
        }
    }
}