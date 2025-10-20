package com.example.controldegastosv2.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.*

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Resumen : Screen("resumen", Icons.Filled.Info, "Resumen")
    object Historial : Screen("historial", Icons.Filled.List, "Historial")
    object Agregar : Screen("agregar", Icons.Filled.Add, "Agregar")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Resumen, Screen.Historial, Screen.Agregar)
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Resumen.route, Modifier.padding(innerPadding)) {
            composable(Screen.Resumen.route) { ResumenScreen() }
            composable(Screen.Historial.route) { ListaGastosScreen() }
            composable(Screen.Agregar.route) { AgregarGastoScreen(navController) }
        }
    }
}