package com.example.tt1
import android.content.BroadcastReceiver
import android.content.Context
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.tt1.tarea.TareaActivity

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reminder_channel"

        // Crear el canal de notificación si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Recordatorios", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // Crear el Intent para abrir la aplicación al tocar la notificación
        val notificationIntent = Intent(context, TareaActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(titulo)
            .setContentText(descripcion)
            .setSmallIcon(R.drawable.ic_notification) // Cambia el ícono según tu diseño
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}
