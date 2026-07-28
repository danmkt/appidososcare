package com.example.appcuidadoidosos.viewmodel

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.Meal
import com.example.appcuidadoidosos.database.Medication
import com.example.appcuidadoidosos.database.Water
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SharedViewModel(private val db: AppDatabase) {

    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val queries = db.appDatabaseQueries

    @NativeCoroutines
    val medicationState: StateFlow<List<Medication>> =
        queries.selectAllMedications()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    @NativeCoroutines
    val waterState: StateFlow<List<Water>> =
        queries.selectAllWater()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    @NativeCoroutines
    val mealState: StateFlow<List<Meal>> =
        queries.selectAllMeals()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    private fun now(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}"
    }

    fun addWater() {
        viewModelScope.launch(Dispatchers.IO) {
            queries.insertWater(now())
        }
    }

    fun deleteWater(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            queries.deleteWaterById(id)
        }
    }

    fun addMeal(type: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            queries.insertMeal(type, description, now())
        }
    }

    fun deleteMeal(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            queries.deleteMealById(id)
        }
    }

    fun addMedication(name: String, time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            queries.insertMedication(name, time)
        }
    }

    fun deleteMedication(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            queries.deleteMedicationById(id)
        }
    }

    fun toggleMedicationTaken(id: Long, isTaken: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            queries.updateMedicationTaken(if (isTaken) 1L else 0L, id)
        }
    }
}