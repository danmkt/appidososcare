package com.example.appcuidadoidosos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appcuidadoidosos.database.Medication

@Composable
fun MedicationScreen(
    medicationState: List<Medication>,
    onAddMedication: (name: String, time: String) -> Unit,
    onDeleteMedication: (Long) -> Unit,
    onToggleTaken: (id: Long, isTaken: Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Medicamento")
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
                    text = "Registros de Medicamentos",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            if (medicationState.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum medicamento registrado.\nClique no botão '+' para adicionar.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(medicationState) { medicationLog ->
                    MedicationLogItem(
                        medicationLog = medicationLog,
                        onDelete = { onDeleteMedication(medicationLog.id) },
                        onToggleTaken = { isTaken -> onToggleTaken(medicationLog.id, isTaken) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddMedicationDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, time ->
                onAddMedication(name, time)
                showDialog = false
            }
        )
    }
}

@Composable
private fun MedicationLogItem(
    medicationLog: Medication,
    onDelete: () -> Unit,
    onToggleTaken: (Boolean) -> Unit
) {
    val isTaken = medicationLog.isTaken

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Outlined.Medication,
                contentDescription = "Ícone de medicamento",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicationLog.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Horário: ${medicationLog.time}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isTaken,
                onCheckedChange = onToggleTaken,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Deletar medicamento",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun AddMedicationDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, time: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Medicamento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do medicamento") },
                    placeholder = { Text("Ex: Paracetamol") }
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Horário") },
                    placeholder = { Text("Ex: 08:00") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && time.isNotBlank()) {
                        onConfirm(name, time)
                    }
                },
                enabled = name.isNotBlank() && time.isNotBlank()
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