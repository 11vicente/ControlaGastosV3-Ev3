package com.example.controldegastosv2.ui.theme.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controldegastosv2.data.Gasto
import com.example.controldegastosv2.data.GastoRepository
import kotlinx.coroutines.launch

class AgregarGastoViewModel : ViewModel() {

    private val repository = GastoRepository()

    /**
     * Crea un gasto en el backend usando Retrofit.
     *
     * @param gasto objeto con los datos ingresados en el formulario
     * @param onResult callback que indica si la creación fue exitosa
     */
    fun crearGasto(gasto: Gasto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.crearGasto(gasto)

            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}
