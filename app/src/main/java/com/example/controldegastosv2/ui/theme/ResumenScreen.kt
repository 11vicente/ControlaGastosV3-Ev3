package com.example.controldegastosv2.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // 1. Importa LazyColumn
import androidx.compose.foundation.lazy.items // 2. Importa la función 'items'
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import com.example.controldegastosv2.external.IndicadoresViewModel

import com.example.controldegastosv2.ui.theme.viewmodels.GastoViewModel
import java.util.Locale // Importa Locale para formatear números de forma segura

@Composable
fun ResumenScreen(
    gastoViewModel: GastoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Observamos LiveData como State
    val gastos by gastoViewModel.gastos.observeAsState(emptyList())

    // ==== CÁLCULOS LOCALES ====

    val totalGastos = gastos.sumOf { it.monto }

    val categoriasAgrupadas = gastos
        .groupBy { it.categoria }
        .mapValues { entry ->
            entry.value.sumOf { it.monto }
        }
    val indicadoresVM = remember { IndicadoresViewModel() }
    val valorDolar = indicadoresVM.dolar.value
    val valorUF = indicadoresVM.uf.value
    val errorIndicadores = indicadoresVM.error.value

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Text("Resumen de Gastos", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        Card(
            Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("💰 Total gastado:", style = MaterialTheme.typography.bodyLarge)

                Text(
                    // Es buena práctica usar Locale para el formato
                    "$${String.format(Locale.US, "%.2f", totalGastos)}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text("📡 Indicadores Económicos", style = MaterialTheme.typography.titleMedium)

        if (valorDolar != null && valorUF != null) {
            Text("Dólar: $${valorDolar}")
            Text("UF: $${valorUF}")
        } else if (errorIndicadores != null) {
            Text(
                text = errorIndicadores,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Text("Cargando indicadores…")
        }

        Spacer(Modifier.height(20.dp))

        Text("Por categoría", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(10.dp))

        // --- INICIO DE LA CORRECCIÓN ---
        // Se reemplaza el bucle forEach por un LazyColumn para renderizar la lista de forma eficiente.
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Añade un espacio uniforme entre cada tarjeta
        ) {
            // Usamos 'items' para iterar sobre la lista.
            // Convertimos el mapa 'categoriasAgrupadas' a una lista de pares (Pair).

            // SOLUCIÓN: Especificamos explícitamente los tipos para 'categoria' y 'montoTotal'.
            items(categoriasAgrupadas.toList()) { (categoria: String, montoTotal: Double) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(categoria)
                        Text("$${String.format(Locale.US, "%.2f", montoTotal)}")
                    }
                }
            }
        }
        // --- FIN DE LA CORRECCIÓN ---
    }
}
