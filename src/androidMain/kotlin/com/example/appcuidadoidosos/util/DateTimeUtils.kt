package com.example.appcuidadoidosos.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun getCurrentDateString(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

actual fun getCurrentTimeString(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date())
}
