package com.example.appcuidadoidosos.widget

import android.content.Context
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.DatabaseDriverFactory
import com.example.appcuidadoidosos.util.getCurrentDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class WidgetData(
    val waterTotalMl: Long,
    val waterGoalMl: Long,
    val medicationsTaken: Int,
    val medicationsTotal: Int
)

class WidgetDataRepository(context: Context) {

    private val database = AppDatabase(DatabaseDriverFactory(context).createDriver())

    suspend fun getWidgetData(): WidgetData = withContext(Dispatchers.IO) {
        val todayDate = getCurrentDateString()

        val totalWaterResult = database.appDatabaseQueries.getTotalWaterByDate(todayDate).executeAsOne()
        val medications = database.appDatabaseQueries.selectAllMedications().executeAsList()

        WidgetData(
            waterTotalMl = totalWaterResult.totalMl ?: 0L,
            waterGoalMl = 2000L, // Meta fixa por enquanto
            medicationsTaken = medications.count { it.isTaken != 0L },
            medicationsTotal = medications.size
        )
    }
}
