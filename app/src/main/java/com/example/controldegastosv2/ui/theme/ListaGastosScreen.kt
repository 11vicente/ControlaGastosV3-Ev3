package com.example.controldegastosv2.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controldegastosv2.data.Gasto
import com.example.controldegastosv2.ui.theme.viewmodels.GastoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGastosScreen(
    viewModel: GastoViewModel = viewModel(),
    onAgregarGasto: () -> Unit
) {
    val gastos by viewModel.gastos.observeAsState(emptyList())
    val error by viewModel.error.observeAsState(initial = null)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarGasto) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Mis Gastos",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            // Mostrar error si aparece
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (gastos.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay gastos registrados")
                }
            } else {
                LazyColumn {
                    items(gastos) { gasto ->
                        GastoItem(
                            gasto = gasto,
                            onDelete = { viewModel.eliminarGasto(gasto.id!!) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: Gasto,
    onDelete: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(gasto.descripcion, style = MaterialTheme.typography.titleMedium)
                Text("Categoría: ${gasto.categoria}")
                Text("Monto: $${gasto.monto}")
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar gasto"
                )
            }
        }
    }
}
