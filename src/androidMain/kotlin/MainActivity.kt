package com.example.appcuidadoidosos

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.DatabaseDriverFactory
import com.example.appcuidadoidosos.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase(DatabaseDriverFactory(applicationContext).createDriver())
        val viewModel = SharedViewModel(db)

        setContent {
            App(viewModel)
        }
    }
}