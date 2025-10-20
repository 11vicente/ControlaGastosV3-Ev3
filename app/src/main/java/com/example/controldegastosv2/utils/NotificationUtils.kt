package com.example.controldegastosv2.utils



import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun showGastoNotification(context: Context, descripcion: String, monto: Double) {
    val channelId = "gastos_channel"
    val notificationId = 777

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Gastos"
        val descriptionText = "Notificaciones de gastos"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Nuevo gasto agregado")
        .setContentText("$descripcion: $$monto")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}