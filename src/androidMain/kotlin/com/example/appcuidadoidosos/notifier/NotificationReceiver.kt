package com.example.appcuidadoidosos.notifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Lembrete"
        val message = intent.getStringExtra("message") ?: "Hora de cuidar da saúde!"
        val notifier = AndroidNotifier(context)
        notifier.showNotification(title, message)
    }
}
