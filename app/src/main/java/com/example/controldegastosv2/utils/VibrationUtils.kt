package com.example.controldegastosv2.utils
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator




import android.util.Log



@Suppress("MissingPermission")
fun vibrate(context: Context, duration: Long = 100) {
    try {
        // Obtener el servicio Vibrator (puede ser null en dispositivos sin motor de vibración).
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let {
            // Para API 26+ usar VibrationEffect (más control).
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                // Método deprecado para compatibilidad con versiones antiguas.
                @Suppress("DEPRECATION")
                it.vibrate(duration)
            }
        }
    } catch (e: Exception) {
        // Capturamos excepciones (p. ej. SecurityException si falta el permiso) para evitar crashes.
        Log.e("Vibrate", "Error al vibrar: ", e)
    }
}