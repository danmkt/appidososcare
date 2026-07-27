package com.example.appcuidadoidosos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appcuidadoidosos.database.Meal

@Composable
fun MealScreen(
    mealState: List<Meal>,
    onAddMeal: (type: String, description: String) -> Unit,
    onDeleteMeal: (Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Refeição")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Registros de Refeição",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            if (mealState.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhuma refeição registrada hoje.\nClique no botão '+' para adicionar.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(mealState) { mealLog ->
                    MealLogItem(
                        mealLog = mealLog,
                        onDelete = { onDeleteMeal(mealLog.id) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddMealDialog(
            onDismiss = { showDialog = false },
            onConfirm = { type, description ->
                onAddMeal(type, description)
                showDialog = false
            }
        )
    }
}

@Composable
private fun MealLogItem(
    mealLog: Meal,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.RestaurantMenu,
                    contentDescription = "Ícone de refeição",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Column {
                    Text(
                        text = mealLog.type,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = mealLog.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Deletar refeição",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun AddMealDialog(
    onDismiss: () -> Unit,
    onConfirm: (type: String, description: String) -> Unit
) {
    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Refeição") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tipo da refeição") },
                    placeholder = { Text("Ex: Almoço, Jantar...") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    placeholder = { Text("Ex: Arroz, feijão e salada") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (type.isNotBlank() && description.isNotBlank()) {
                        onConfirm(type, description)
                    }
                },
                enabled = type.isNotBlank() && description.isNotBlank()
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}