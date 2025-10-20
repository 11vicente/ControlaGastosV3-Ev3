package com.example.controldegastosv2.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controldegastosv2.data.GastoEntity


import java.text.SimpleDateFormat
import java.util.*
import com.example.controldegastosv2.ui.theme.viewmodels.GastoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGastosScreen(
    gastoViewModel: GastoViewModel = viewModel()
) {
    val gastos by gastoViewModel.gastos.observeAsState(emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Lista de Gastos") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(gastos) { gasto ->
                GastoItem(gasto)
            }
        }
    }
}

@Composable
fun GastoItem(gasto: GastoEntity) {
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val fechaString = remember(gasto.fecha) { sdf.format(Date(gasto.fecha)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = gasto.descripcion, style = MaterialTheme.typography.titleLarge)
            Text(text = "Monto: $${gasto.monto}")
            Text(text = "Categoría: ${gasto.categoria}")
            Text(text = "Fecha: $fechaString")
        }
    }
}