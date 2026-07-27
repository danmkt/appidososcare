package com.example.appcuidadoidosos.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

actual fun getCurrentDateString(): String {
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd"
    }
    return formatter.stringFromDate(NSDate())
}

actual fun getCurrentTimeString(): String {
    val formatter = NSDateFormatter().apply {
        dateFormat = "HH:mm"
    }
    return formatter.stringFromDate(NSDate())
}
