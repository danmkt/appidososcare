package com.example.appcuidadoidosos.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class AppStrings(
    val dashboard: String,
    val medication: String,
    val water: String,
    val meals: String,
    val taken: String,
    val addMedication: String,
    val waterGoal: String,
    val addWater: String,
    val addMeal: String
)

private val ptStrings = AppStrings(
    dashboard = "Resumo",
    medication = "Medicamentos",
    water = "Água",
    meals = "Refeições",
    taken = "Tomado",
    addMedication = "Adicionar Medicamento",
    waterGoal = "Meta de Água",
    addWater = "Adicionar Água",
    addMeal = "Adicionar Refeição"
)

private val enStrings = AppStrings(
    dashboard = "Dashboard",
    medication = "Medication",
    water = "Water",
    meals = "Meals",
    taken = "Taken",
    addMedication = "Add Medication",
    waterGoal = "Water Goal",
    addWater = "Add Water",
    addMeal = "Add Meal"
)

val LocalStrings = staticCompositionLocalOf { ptStrings }
