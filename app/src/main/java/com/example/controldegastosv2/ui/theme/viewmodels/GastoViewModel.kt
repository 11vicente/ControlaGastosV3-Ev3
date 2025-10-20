package com.example.controldegastosv2.ui.theme.viewmodels



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import com.example.controldegastosv2.data.AppDatabase
import com.example.controldegastosv2.data.GastoEntity
import com.example.controldegastosv2.data.GastoRepository
import kotlinx.coroutines.launch

class GastoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GastoRepository

    val gastos: LiveData<List<GastoEntity>>


    init {
        val gastoDao = AppDatabase.getDatabase(application).gastoDao()
        repository = GastoRepository(gastoDao)
        gastos = repository.obtenerGastos()
    }

    fun agregarGasto(gasto: GastoEntity) = viewModelScope.launch {
        repository.insertarGasto(gasto)
    }
}