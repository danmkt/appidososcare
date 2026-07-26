package com.example.appcuidadoidosos.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory actual constructor(context: Any?) {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:app.db")
        if (!File("app.db").exists()) {
            AppDatabase.Schema.create(driver)
        }
        return driver
    }
}
