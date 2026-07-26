package com.example.appcuidadoidosos

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcuidadoidosos.notifier.Notifier
import com.example.appcuidadoidosos.ui.screens.DashboardScreen
import com.example.appcuidadoidosos.ui.screens.MealScreen
import com.example.appcuidadoidosos.ui.screens.MedicationScreen
import com.example.appcuidadoidosos.ui.screens.WaterScreen
import com.example.appcuidadoidosos.ui.theme.*
import com.example.appcuidadoidosos.viewmodel.SharedViewModel

sealed class Screen(val title: (AppStrings) -> String, val icon: ImageVector) {
    object Dashboard : Screen({ it.dashboard }, Icons.Default.Home)
    object Medications : Screen({ it.medication }, Icons.Default.Favorite)
    object Water : Screen({ it.water }, Icons.Default.Info)
    object Meals : Screen({ it.meals }, Icons.Default.List)
}

@Composable
fun App(viewModel: SharedViewModel, notifier: Notifier) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    val medications by viewModel.medications.collectAsState()
    val waterLogs by viewModel.waterLogs.collectAsState()
    val totalWaterMl by viewModel.totalWaterMl.collectAsState()
    val mealLogs by viewModel.mealLogs.collectAsState()

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
                    Screen.Dashboard -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DashboardScreen(
                            waterTotalMl = totalWaterMl,
                            waterGoalMl = 2000L,
                            medicationsTakenCount = medications.count { it.isTaken != 0L },
                            medicationsTotalCount = medications.size,
                            mealsLoggedCount = mealLogs.size,
                            onNavigateToWater = { currentScreen = Screen.Water },
                            onNavigateToMedications = { currentScreen = Screen.Medications },
                            onNavigateToMeals = { currentScreen = Screen.Meals }
                        )
                        // TODO: Remove this test button
                        Button(onClick = {
                            notifier.showNotification("Teste", "Esta é uma notificação de teste.")
                        }) {
                            Text("Testar Notificação")
                        }
                    }

                    Screen.Medications -> MedicationScreen(
                        medications = medications,
                        onAddMedication = viewModel::addMedication,
                        onToggleTaken = viewModel::toggleMedicationTaken,
                        onDeleteMedication = viewModel::deleteMedication,
                        onResetAllTaken = viewModel::resetAllMedicationsTaken
                    )

                    Screen.Water -> WaterScreen(
                        waterLogs = waterLogs,
                        totalWaterMl = totalWaterMl,
                        dailyGoalMl = 2000L,
                        onAddWater = viewModel::addWater,
                        onDeleteWaterLog = viewModel::deleteWaterLog
                    )

                    Screen.Meals -> MealScreen(
                        mealLogs = mealLogs,
                        onAddMeal = viewModel::addMeal,
                        onToggleConsumed = viewModel::toggleMealConsumed,
                        onDeleteMeal = viewModel::deleteMeal
                    )
                }
            }
        }
    }
}
