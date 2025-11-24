package com.example.controldegastosv2.ui.theme



sealed class Screen(val route: String) {

    object Lista : Screen("lista_gastos")
    object Agregar : Screen("agregar_gasto")
    object Resumen : Screen("resumen_gastos")

}
