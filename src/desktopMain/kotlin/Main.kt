package com.example.appcuidadoidosos

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.DatabaseDriverFactory

fun main() = application {
    val database = AppDatabase(DatabaseDriverFactory().createDriver())
    Window(onCloseRequest = ::exitApplication, title = "app-cuidado-idosos") {
        App(database)
    }
}
