package com.example.appcuidadoidosos.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory actual constructor(context: Any?) {
    actual fun createDriver(): SqlDriver {
        val dbFile = File("app.db")
        val isNew = !dbFile.exists()
        val driver = JdbcSqliteDriver("jdbc:sqlite:app.db")
        if (isNew) {
            AppDatabase.Schema.create(driver)
        }
        return driver
    }
}
