package com.example.appcuidadoidosos.notifier

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.NSRunLoop
import platform.darwin.NSUUID

actual class Notifier {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    fun requestAuthorization() {
        notificationCenter.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        ) { granted, error ->
            if (granted) {
                println("Notification authorization granted.")
            } else {
                error?.let { println(it.localizedDescription) }
            }
        }
    }

    actual fun showNotification(title: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
        }
        val uuid = NSUUID.UUID().UUIDString()
        val request = UNNotificationRequest.requestWithIdentifier(uuid, content, null)

        notificationCenter.addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error showing notification: ${error.localizedDescription}")
            }
        }
    }
}
