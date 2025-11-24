package com.example.controldegastosv2.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = navController.currentDestination?.route == "lista",
            onClick = { navController.navigate("lista") },
            label = { Text("Lista") },
            icon = { Icon(Icons.Default.List, contentDescription = null) }
        )
        NavigationBarItem(
            selected = navController.currentDestination?.route == "agregar",
            onClick = { navController.navigate("agregar") },
            label = { Text("Agregar") },
            icon = { Icon(Icons.Default.Add, contentDescription = null) }
        )
        NavigationBarItem(
            selected = navController.currentDestination?.route == "resumen",
            onClick = { navController.navigate("resumen") },
            label = { Text("Resumen") },
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) }
        )
    }
}
