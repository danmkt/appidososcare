package com.example.appcuidadoidosos.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory(context: Any? = null) {
    fun createDriver(): SqlDriver
}
