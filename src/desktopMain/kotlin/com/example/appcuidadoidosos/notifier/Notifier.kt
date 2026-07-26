package com.example.appcuidadoidosos.notifier

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon

actual class Notifier {
    private val trayIcon: TrayIcon?

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

    actual fun showNotification(title: String, message: String) {
        trayIcon?.displayMessage(title, message, TrayIcon.MessageType.INFO)
    }
}
