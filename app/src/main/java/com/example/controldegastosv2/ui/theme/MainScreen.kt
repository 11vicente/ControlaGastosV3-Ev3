package com.example.controldegastosv2.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.controldegastosv2.ui.theme.viewmodels.GastoViewModel
import com.example.controldegastosv2.ui.theme.viewmodels.*
import com.example.controldegastosv2.ui.theme.AgregarGastoScreen
import com.example.controldegastosv2.ui.theme.ListaGastosScreen

@Composable
fun MainScreen(
    gastoViewModel: GastoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Cargar los gastos al iniciar la app — solo 1 vez
    LaunchedEffect(Unit) {
        gastoViewModel.cargarGastos()
    }

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "lista",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("lista") {
                ListaGastosScreen(
                    viewModel = gastoViewModel,
                    onAgregarGasto = { navController.navigate("agregar") }
                )
            }
            composable("agregar") {
                AgregarGastoScreen(navController, gastoViewModel)
            }
            composable("resumen") {
                ResumenScreen(gastoViewModel)
            }
        }
    }
}