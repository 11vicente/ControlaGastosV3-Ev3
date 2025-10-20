package com.example.controldegastosv2.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controldegastosv2.ui.theme.viewmodels.ResumenViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun ResumenScreen(
    resumenViewModel: ResumenViewModel = viewModel()
) {
    val totalGastado by resumenViewModel.totalGastado.observeAsState(0.0)
    val ultimoGasto by resumenViewModel.ultimoGasto.observeAsState()
    val gastoMaximo by resumenViewModel.gastoMaximo.observeAsState()
    val todosLosGastos by resumenViewModel.todosLosGastos.observeAsState(emptyList())

    val pieEntries = remember(todosLosGastos) {
        todosLosGastos.groupBy { it.categoria }
            .map { (cat, lista) -> PieEntry(lista.sumOf { it.monto }.toFloat(), cat) }
    }

    val dataSet = PieDataSet(pieEntries, "").apply {
        setColors(*ColorTemplate.MATERIAL_COLORS)
    }
    val pieData = PieData(dataSet)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Total gastado: $${"%.2f".format(totalGastado)}", style = MaterialTheme.typography.titleLarge)
        Text("Último gasto: ${ultimoGasto?.monto ?: "---"}")
        Text("Gasto más alto: ${gastoMaximo?.monto ?: "---"}")
        Spacer(Modifier.height(16.dp))
        AndroidView(factory = { context ->
            PieChart(context).apply {
                data = pieData
                description.isEnabled = false
                centerText = "Gastos"
                animateY(1000)
            }
        }, update = { chart ->
            chart.data = pieData
            chart.invalidate()
        }, modifier = Modifier.height(300.dp).fillMaxWidth())
    }
}