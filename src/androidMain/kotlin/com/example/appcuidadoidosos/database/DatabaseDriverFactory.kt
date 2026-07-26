package com.example.appcuidadoidosos.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory actual constructor(private val context: Any?) {
    actual fun createDriver(): SqlDriver {
        require(context is Context) { "Android Context is required for DatabaseDriverFactory" }
        return AndroidSqliteDriver(AppDatabase.Schema, context, "app.db")
    }
}
