package com.example.controldegastosv2.ui.theme

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controldegastosv2.ui.theme.viewmodels.GastoViewModel
import com.example.controldegastosv2.data.GastoEntity

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay

import androidx.compose.ui.platform.LocalContext
import android.os.Vibrator
import android.os.VibrationEffect
import com.example.controldegastosv2.utils.vibrate

data class GastoFormState(
    val descripcion: String = "",
    val monto: String = "",
    val categoria: String = "",
    val fotoUri: Uri? = null,
    val descripcionError: String? = null,
    val montoError: String? = null,
    val categoriaError: String? = null
)

// Lógica de validación desacoplada
fun validarGastoForm(descripcion: String, monto: String, categoria: String): GastoFormState {
    var descripcionError: String? = null
    var montoError: String? = null
    var categoriaError: String? = null

    if (descripcion.isBlank()) descripcionError = "Ingrese una descripción"
    if (monto.isBlank()) montoError = "Ingrese un monto"
    else if (monto.toDoubleOrNull() == null || monto.toDoubleOrNull()!! <= 0) montoError = "Monto inválido"
    if (categoria.isBlank()) categoriaError = "Seleccione una categoría"

    return GastoFormState(
        descripcion = descripcion,
        monto = monto,
        categoria = categoria,
        descripcionError = descripcionError,
        montoError = montoError,
        categoriaError = categoriaError
    )
}

@Composable
fun AgregarGastoScreen(
    navController: NavController,
    gastoViewModel: GastoViewModel = viewModel()
) {

    val context = LocalContext.current
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    val categorias = listOf("Comida", "Transporte", "Entretenimiento", "Otros")
    var categoria by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var fotoFile by remember { mutableStateOf<File?>(null) }
    var formError by remember { mutableStateOf<GastoFormState?>(null) }
    var submitAttempted by remember { mutableStateOf(false) }
    var guardarExitoso by remember { mutableStateOf(false) }
    // Imagen preview
    val bitmap = remember(fotoUri) {
        fotoUri?.let {
            val stream = context.contentResolver.openInputStream(it)
            stream?.use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
        }
    }

    val tomarFotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        // fotoUri ya contiene la Uri
    }

    fun crearUriParaFoto(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("gasto_${timeStamp}_", ".jpg", storageDir)
        fotoFile = file
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    fun resetForm() {
        descripcion = ""
        monto = ""
        categoria = ""
        fotoUri = null
        fotoFile = null
        formError = null
        submitAttempted = false
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                if (submitAttempted) formError = validarGastoForm(descripcion, monto, categoria)
            },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError?.descripcionError != null,
            trailingIcon = {
                if (formError?.descripcionError != null)
                    Icon(Icons.Filled.Error, "error", tint = Color.Red)
                else
                    Icon(Icons.Filled.TextFields, "desc")
            }
        )
        formError?.descripcionError?.let { error ->
            Text(error, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = monto,
            onValueChange = {
                // Solo acepta números y punto
                if (it.all { c -> c.isDigit() || c == '.' }) {
                    monto = it
                    if (submitAttempted) formError = validarGastoForm(descripcion, monto, categoria)
                }
            },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError?.montoError != null,
            trailingIcon = {
                if (formError?.montoError != null)
                    Icon(Icons.Filled.Error, "error", tint = Color.Red)
                else
                    Icon(Icons.Filled.AttachMoney, "monto")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        formError?.montoError?.let { error ->
            Text(error, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(8.dp))
        // Menú desplegable de categoría
        OutlinedTextField(
            value = categoria,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            label = { Text("Categoría") },
            readOnly = true,
            isError = formError?.categoriaError != null,
            trailingIcon = {
                if (formError?.categoriaError != null)
                    Icon(Icons.Filled.Error, "error", tint = Color.Red)
                else
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
                        if (submitAttempted) formError = validarGastoForm(descripcion, monto, categoria)
                    }
                )
            }
        }
        formError?.categoriaError?.let { error ->
            Text(error, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                val nuevaUri = crearUriParaFoto()
                fotoUri = nuevaUri
                tomarFotoLauncher.launch(nuevaUri)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.PhotoCamera, null)
            Spacer(Modifier.width(8.dp))
            Text("Tomar foto")
        }

        fotoUri?.let {
            bitmap?.let { bmp ->
                Image(bmp, contentDescription = "Foto", modifier = Modifier.height(120.dp).fillMaxWidth())
            }
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                submitAttempted = true
                val validation = validarGastoForm(descripcion, monto, categoria)
                formError = validation
                if (validation.descripcionError == null && validation.montoError == null && validation.categoriaError == null) {
                    val gasto = GastoEntity(
                        descripcion = descripcion,
                        monto = monto.toDouble(),
                        categoria = categoria,
                        fecha = System.currentTimeMillis(),
                        fotoUri = fotoUri?.toString()
                    )
                    gastoViewModel.agregarGasto(gasto)
                    guardarExitoso = true


                    resetForm()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar") }
    }

    // ANIMACIÓN Y NAVEGACIÓN AUTOMÁTICA
    if (guardarExitoso) {
        vibrate(context)
        LaunchedEffect(guardarExitoso) {
            delay(400) // pequeña pausa para feedback visual
            navController.navigate(Screen.Resumen.route) {
                popUpTo(Screen.Resumen.route) { inclusive = false }
                launchSingleTop = true



            }
        }
    }
}
