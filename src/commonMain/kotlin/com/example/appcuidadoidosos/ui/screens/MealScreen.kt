package com.example.appcuidadoidosos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcuidadoidosos.database.Meal_logs
import com.example.appcuidadoidosos.ui.theme.*

@Composable
fun MealScreen(
    mealLogs: List<Meal_logs>,
    onAddMeal: (mealType: String, description: String, time: String) -> Unit,
    onToggleConsumed: (id: Long, isConsumed: Boolean) -> Unit,
    onDeleteMeal: (id: Long) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabeçalho
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Registro de Alimentação",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = AccentOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Registrar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mealLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma refeição registrada hoje.\nClique em 'Registrar' para acompanhar o dia alimentício.",
                    fontSize = 17.sp,
                    color = TextLight
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(mealLogs) { meal ->
                    MealItemCard(
                        meal = meal,
                        onToggleConsumed = { onToggleConsumed(meal.id, meal.isConsumed == 0L) },
                        onDelete = { onDeleteMeal(meal.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddMealDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { type, desc, time ->
                onAddMeal(type, desc, time)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun MealItemCard(
    meal: Meal_logs,
    onToggleConsumed: () -> Unit,
    onDelete: () -> Unit
) {
    val isConsumed = meal.isConsumed != 0L

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 3.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isConsumed) SecondaryLight else SurfaceCard
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onToggleConsumed,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = if (isConsumed) Icons.Default.CheckCircle else Icons.Default.Clear,
                    contentDescription = null,
                    tint = if (isConsumed) SecondaryGreen else TextLight,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.mealType,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentOrange
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = meal.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = "Horário: ${meal.time}",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = RedAlert)
            }
        }
    }
}

@Composable
fun AddMealDialog(
    onDismiss: () -> Unit,
    onConfirm: (mealType: String, description: String, time: String) -> Unit
) {
    val mealTypes = listOf("Café da Manhã", "Almoço", "Lanche da Tarde", "Jantar", "Ceia")
    var selectedType by remember { mutableStateOf(mealTypes.first()) }
    var description by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("08:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Registrar Refeição",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AccentOrange
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Tipo de Refeição:", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)

                // Botões de seleção do tipo
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    mealTypes.chunked(2).forEach { rowTypes ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowTypes.forEach { type ->
                                val isSelected = selectedType == type
                                Button(
                                    onClick = { selectedType = type },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = if (isSelected) AccentOrange else PrimaryLight,
                                        contentColor = if (isSelected) Color.White else PrimaryDark
                                    ),
                                    elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(type, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("O que foi consumido?") },
                    placeholder = { Text("Ex: Sopa de legumes, 1 fruta...") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Horário (ex: 12:30)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (description.isNotBlank()) {
                        onConfirm(selectedType, description, time)
                    }
                },
                enabled = description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(backgroundColor = AccentOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salvar", color = Color.White, fontSize = 16.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontSize = 16.sp, color = TextSecondary)
            }
        }
    )
}
