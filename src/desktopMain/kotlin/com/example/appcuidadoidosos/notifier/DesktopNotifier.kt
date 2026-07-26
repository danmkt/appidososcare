package com.example.appcuidadoidosos.notifier

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

class DesktopNotifier : Notifier {
    private val trayIcon: TrayIcon?
    private val timers = mutableMapOf<String, Timer>()

    init {
        if (SystemTray.isSupported()) {
            val image = Toolkit.getDefaultToolkit().createImage("icon.png") // Placeholder icon
            trayIcon = TrayIcon(image, "Elderly Care App")
            trayIcon.isImageAutoSize = true
            SystemTray.getSystemTray().add(trayIcon)
        } else {
            trayIcon = null
        }
    }

    override fun showNotification(title: String, message: String) {
        trayIcon?.displayMessage(title, message, TrayIcon.MessageType.INFO)
    }

    override fun scheduleNotification(id: String, hour: Int, minute: Int, title: String, message: String) {
        val timer = Timer(id)
        val task = object : TimerTask() {
            override fun run() {
                showNotification(title, message)
            }
        }
        val date = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }
        timer.schedule(task, date.time, 1000 * 60 * 60 * 24) // Repeat daily
        timers[id] = timer
    }

    override fun cancelNotification(id: String) {
        timers[id]?.cancel()
        timers.remove(id)
    }
}
