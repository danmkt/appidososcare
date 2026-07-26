package com.example.appcuidadoidosos.viewmodel

import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.Meal_logs
import com.example.appcuidadoidosos.database.Medications
import com.example.appcuidadoidosos.database.Water_logs
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(private val database: AppDatabase, private val coroutineScope: CoroutineScope) {

    private val _medications = MutableStateFlow<List<Medications>>(emptyList())
    @NativeCoroutines
    val medications: StateFlow<List<Medications>> = _medications

    private val _waterLogs = MutableStateFlow<List<Water_logs>>(emptyList())
    @NativeCoroutines
    val waterLogs: StateFlow<List<Water_logs>> = _waterLogs

    private val _totalWaterMl = MutableStateFlow(0L)
    @NativeCoroutines
    val totalWaterMl: StateFlow<Long> = _totalWaterMl

    private val _mealLogs = MutableStateFlow<List<Meal_logs>>(emptyList())
    @NativeCoroutines
    val mealLogs: StateFlow<List<Meal_logs>> = _mealLogs

    private val todayDate = "2026-07-26" // Data para persistência

    init {
        refreshData()
    }

    fun refreshData() {
        coroutineScope.launch {
            val (loadedMeds, loadedWater, loadedWaterTotal, loadedMeals) = withContext(Dispatchers.IO) {
                val meds = database.appDatabaseQueries.selectAllMedications().executeAsList()
                val water = database.appDatabaseQueries.selectWaterLogsByDate(todayDate).executeAsList()
                val totalWaterResult = database.appDatabaseQueries.getTotalWaterByDate(todayDate).executeAsOne()
                val meals = database.appDatabaseQueries.selectMealLogsByDate(todayDate).executeAsList()
                Tuple4(meds, water, totalWaterResult.totalMl ?: 0L, meals)
            }
            _medications.value = loadedMeds
            _waterLogs.value = loadedWater
            _totalWaterMl.value = loadedWaterTotal
            _mealLogs.value = loadedMeals
        }
    }

    fun addMedication(name: String, dosage: String, frequency: String, timeOfDay: String, notes: String?) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.insertMedication(name, dosage, frequency, timeOfDay, 0L, notes)
            }
            refreshData()
        }
    }

    fun toggleMedicationTaken(id: Long, isTaken: Boolean) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.updateMedicationTakenStatus(if (isTaken) 1L else 0L, id)
            }
            refreshData()
        }
    }

    fun deleteMedication(id: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.deleteMedicationById(id)
            }
            refreshData()
        }
    }

    fun resetAllMedicationsTaken() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.resetMedicationsTakenStatus()
            }
            refreshData()
        }
    }

    fun addWater(amountMl: Int) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val timestamp = "12:00"
                database.appDatabaseQueries.insertWaterLog(amountMl.toLong(), timestamp, todayDate)
            }
            refreshData()
        }
    }

    fun deleteWaterLog(id: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.deleteWaterLogById(id)
            }
            refreshData()
        }
    }

    fun addMeal(mealType: String, description: String, time: String) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.insertMealLog(mealType, description, time, todayDate, 1L)
            }
            refreshData()
        }
    }

    fun toggleMealConsumed(id: Long, isConsumed: Boolean) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.updateMealConsumedStatus(if (isConsumed) 1L else 0L, id)
            }
            refreshData()
        }
    }

    fun deleteMeal(id: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                database.appDatabaseQueries.deleteMealLogById(id)
            }
            refreshData()
        }
    }
}

private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
