package com.example.appcuidadoidosos.notifier

class FakeNotifier : Notifier() {
    val notifications = mutableListOf<Notification>()
    val cancelledNotifications = mutableListOf<String>()

    override fun showNotification(title: String, message: String) {
        notifications.add(Notification(title, message))
    }

    override fun scheduleNotification(id: String, hour: Int, minute: Int, title: String, message: String) {
        notifications.add(Notification(title, message, id, hour, minute))
    }

    override fun cancelNotification(id: String) {
        cancelledNotifications.add(id)
    }

    data class Notification(
        val title: String,
        val message: String,
        val id: String? = null,
        val hour: Int? = null,
        val minute: Int? = null
    )
}
