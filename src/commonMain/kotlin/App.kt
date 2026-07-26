package com.example.appcuidadoidosos

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.Meal_logs
import com.example.appcuidadoidosos.database.Medications
import com.example.appcuidadoidosos.database.Water_logs
import com.example.appcuidadoidosos.ui.screens.DashboardScreen
import com.example.appcuidadoidosos.ui.screens.MealScreen
import com.example.appcuidadoidosos.ui.screens.MedicationScreen
import com.example.appcuidadoidosos.ui.screens.WaterScreen
import com.example.appcuidadoidosos.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class Screen(val title: (AppStrings) -> String, val icon: ImageVector) {
    object Dashboard : Screen({ it.dashboard }, Icons.Default.Home)
    object Medications : Screen({ it.medication }, Icons.Default.Favorite)
    object Water : Screen({ it.water }, Icons.Default.Info)
    object Meals : Screen({ it.meals }, Icons.Default.List)
}

@Composable
fun App(database: AppDatabase) {
    val coroutineScope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    // State da aplicação
    var medications by remember { mutableStateOf(emptyList<Medications>()) }
    var waterLogs by remember { mutableStateOf(emptyList<Water_logs>()) }
    var totalWaterMl by remember { mutableStateOf(0L) }
    var mealLogs by remember { mutableStateOf(emptyList<Meal_logs>()) }

    val todayDate = "2026-07-26" // Data para persistência

    // Carregar dados do banco de dados
    fun refreshData() {
        coroutineScope.launch {
            val (loadedMeds, loadedWater, loadedWaterTotal, loadedMeals) = withContext(Dispatchers.IO) {
                val meds = database.appDatabaseQueries.selectAllMedications().executeAsList()
                val water = database.appDatabaseQueries.selectWaterLogsByDate(todayDate).executeAsList()
                val totalWaterResult = database.appDatabaseQueries.getTotalWaterByDate(todayDate).executeAsOne()
                val meals = database.appDatabaseQueries.selectMealLogsByDate(todayDate).executeAsList()
                Tuple4(meds, water, totalWaterResult.totalMl ?: 0L, meals)
            }
            medications = loadedMeds
            waterLogs = loadedWater
            totalWaterMl = loadedWaterTotal
            mealLogs = loadedMeals
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    // Ações de Medicamentos
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

    // Ações de Água
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

    // Ações de Refeições
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

    ElderCareTheme {
        val strings = LocalStrings.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Cuidado Idosos",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    backgroundColor = PrimaryBlue,
                    elevation = 4.dp
                )
            },
            bottomBar = {
                BottomNavigation(
                    backgroundColor = SurfaceCard,
                    elevation = 12.dp
                ) {
                    val screens = listOf(Screen.Dashboard, Screen.Medications, Screen.Water, Screen.Meals)
                    screens.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title(strings),
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            label = {
                                Text(
                                    screen.title(strings),
                                    fontSize = 13.sp,
                                    fontWeight = if (currentScreen == screen) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = currentScreen == screen,
                            selectedContentColor = PrimaryBlue,
                            unselectedContentColor = TextLight,
                            onClick = { currentScreen = screen }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.Dashboard -> DashboardScreen(
                        waterTotalMl = totalWaterMl,
                        waterGoalMl = 2000L,
                        medicationsTakenCount = medications.count { it.isTaken != 0L },
                        medicationsTotalCount = medications.size,
                        mealsLoggedCount = mealLogs.size,
                        onNavigateToWater = { currentScreen = Screen.Water },
                        onNavigateToMedications = { currentScreen = Screen.Medications },
                        onNavigateToMeals = { currentScreen = Screen.Meals }
                    )

                    Screen.Medications -> MedicationScreen(
                        medications = medications,
                        onAddMedication = ::addMedication,
                        onToggleTaken = ::toggleMedicationTaken,
                        onDeleteMedication = ::deleteMedication,
                        onResetAllTaken = ::resetAllMedicationsTaken
                    )

                    Screen.Water -> WaterScreen(
                        waterLogs = waterLogs,
                        totalWaterMl = totalWaterMl,
                        dailyGoalMl = 2000L,
                        onAddWater = ::addWater,
                        onDeleteWaterLog = ::deleteWaterLog
                    )

                    Screen.Meals -> MealScreen(
                        mealLogs = mealLogs,
                        onAddMeal = ::addMeal,
                        onToggleConsumed = ::toggleMealConsumed,
                        onDeleteMeal = ::deleteMeal
                    )
                }
            }
        }
    }
}

private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
