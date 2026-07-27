package com.example.appcuidadoidosos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.appcuidadoidosos.ui.screens.DashboardScreen
import com.example.appcuidadoidosos.ui.screens.MealScreen
import com.example.appcuidadoidosos.ui.screens.MedicationScreen
import com.example.appcuidadoidosos.ui.screens.WaterScreen
import com.example.appcuidadoidosos.ui.theme.AppTheme
import com.example.appcuidadoidosos.viewmodel.SharedViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Outlined.Home)
    object Medications : Screen("medications", "Medicamentos", Icons.Outlined.Medication)
    object Water : Screen("water", "Água", Icons.Outlined.LocalDrink)
    object Meals : Screen("meals", "Refeições", Icons.Outlined.List)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: SharedViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    val waterState by viewModel.waterState.collectAsState()
    val mealState by viewModel.mealState.collectAsState()
    val medicationState by viewModel.medicationState.collectAsState()

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cuidado de Idosos") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    val screens = listOf(Screen.Dashboard, Screen.Medications, Screen.Water, Screen.Meals)
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentScreen == screen,
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
                        waterCount = waterState.size,
                        mealCount = mealState.size,
                        medicationCount = medicationState.size,
                        onNavigate = { screen -> currentScreen = screen }
                    )
                    Screen.Medications -> MedicationScreen(
                        medicationState = medicationState,
                        onAddMedication = { name, time -> viewModel.addMedication(name, time) },
                        onDeleteMedication = { id -> viewModel.deleteMedication(id) },
                        onToggleTaken = { id, isTaken -> viewModel.toggleMedicationTaken(id, isTaken) }
                    )
                    Screen.Water -> WaterScreen(
                        waterState = waterState,
                        onAddWater = { viewModel.addWater() },
                        onDeleteWater = { id -> viewModel.deleteWater(id) }
                    )
                    Screen.Meals -> MealScreen(
                        mealState = mealState,
                        onAddMeal = { type, description -> viewModel.addMeal(type, description) },
                        onDeleteMeal = { id -> viewModel.deleteMeal(id) }
                    )
                }
            }
        }
    }
}