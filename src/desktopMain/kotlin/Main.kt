package com.example.appcuidadoidosos

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.DatabaseDriverFactory
import com.example.appcuidadoidosos.viewmodel.SharedViewModel

fun main() = application {
    val db = AppDatabase(DatabaseDriverFactory().createDriver())
    val viewModel = SharedViewModel(db)

    Window(onCloseRequest = ::exitApplication, title = "Cuidado de Idosos") {
        App(viewModel)
    }
}