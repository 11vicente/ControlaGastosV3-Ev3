package com.example.controldegastosv2.ui.theme

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.controldegastosv2.data.Gasto
import com.example.controldegastosv2.ui.theme.viewmodels.GastoViewModel
import kotlinx.coroutines.delay
import com.example.controldegastosv2.utils.vibrate
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun AgregarGastoScreen(
    navController: NavController,
    gastoViewModel: GastoViewModel = viewModel()
) {
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    val categorias = listOf("Comida", "Transporte", "Entretenimiento", "Otros")
    var categoria by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var descripcionError by remember { mutableStateOf<String?>(null) }
    var montoError by remember { mutableStateOf<String?>(null) }
    var categoriaError by remember { mutableStateOf<String?>(null) }

    var guardarExitoso by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

    }

    Column(Modifier.padding(16.dp)) {

        // DESCRIPCIÓN
        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                descripcionError = if (it.isBlank()) "Ingrese una descripción" else null
            },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            isError = descripcionError != null,
            trailingIcon = {
                if (descripcionError != null)
                    Icon(Icons.Filled.Error, "error", tint = Color.Red)
                else
                    Icon(Icons.Filled.TextFields, "desc")
            }
        )

        descripcionError?.let {
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(8.dp))

        // MONTO
        OutlinedTextField(
            value = monto,
            onValueChange = {
                if (it.all { c -> c.isDigit() || c == '.' }) {
                    monto = it
                    montoError =
                        if (it.isBlank() || it.toDoubleOrNull() == null) "Monto inválido" else null
                }
            },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            isError = montoError != null,
            trailingIcon = {
                if (montoError != null)
                    Icon(Icons.Filled.Error, "error", tint = Color.Red)
                else
                    Icon(Icons.Filled.AttachMoney, "monto")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        montoError?.let {
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(8.dp))

        // CATEGORÍA
        OutlinedTextField(
            value = categoria,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            label = { Text("Categoría") },
            readOnly = true,
            isError = categoriaError != null,
            trailingIcon = {
                Icon(Icons.Filled.Category, "cat")
            }
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categorias.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        categoria = it
                        expanded = false
                        categoriaError = null
                    }
                )
            }
        }

        categoriaError?.let {
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { launcherCamara.launch(null) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar Foto")
        }

        // BOTÓN GUARDAR
        Button(
            onClick = {
                val valido = descripcionError == null &&
                        montoError == null &&
                        categoria.isNotBlank()

                if (!valido) {
                    categoriaError = if (categoria.isBlank()) "Seleccione categoría" else null
                    return@Button
                }

                val gasto = Gasto(
                    descripcion = descripcion,
                    monto = monto.toDouble(),
                    categoria = categoria,
                    fecha = System.currentTimeMillis(),
                    fotoUri = null
                )

                scope.launch {
                    val ok = gastoViewModel.agregarGastoConResultado(gasto)
                    if (ok) {
                        guardarExitoso = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
