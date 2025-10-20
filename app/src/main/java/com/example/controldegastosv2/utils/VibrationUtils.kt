package com.example.controldegastosv2.utils
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator




import android.util.Log
@Suppress("MissingPermission")
fun vibrate(context: Context, duration: Long = 100) {
    try {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(duration)
            }
        }
    } catch (e: Exception) {
        Log.e("Vibrate", "Error al vibrar: ", e)
    }
}