package com.example.appcuidadoidosos.notifier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.appcuidadoidosos.R

actual class Notifier(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "elderly_care_channel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lembretes"
            val descriptionText = "Canal para lembretes de medicação e água"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    actual fun showNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, builder.build())
    }
}
