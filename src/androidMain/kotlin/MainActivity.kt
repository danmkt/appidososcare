package com.example.appcuidadoidosos

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.DatabaseDriverFactory
import com.example.appcuidadoidosos.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase(DatabaseDriverFactory(applicationContext).createDriver())
        val viewModel = SharedViewModel(database, lifecycleScope)
        setContent {
            App(viewModel)
        }
    }
}
