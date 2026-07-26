package com.example.appcuidadoidosos

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.DatabaseDriverFactory
import com.example.appcuidadoidosos.viewmodel.SharedViewModel

fun main() = application {
    val database = AppDatabase(DatabaseDriverFactory().createDriver())
    Window(onCloseRequest = ::exitApplication, title = "app-cuidado-idosos") {
        val coroutineScope = rememberCoroutineScope()
        val viewModel = SharedViewModel(database, coroutineScope)
        App(viewModel)
    }
}
