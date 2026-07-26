package com.example.appcuidadoidosos.notifier

expect class Notifier {
    fun showNotification(title: String, message: String)
    fun scheduleNotification(id: String, hour: Int, minute: Int, title: String, message: String)
    fun cancelNotification(id: String)
}
