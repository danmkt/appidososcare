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
import com.example.appcuidadoidosos.database.Medications
import com.example.appcuidadoidosos.ui.theme.*

@Composable
fun MedicationScreen(
    medications: List<Medications>,
    onAddMedication: (name: String, dosage: String, frequency: String, reminder_time: String, notes: String?) -> Unit,
    onToggleTaken: (id: Long, isTaken: Boolean) -> Unit,
    onDeleteMedication: (id: Long) -> Unit,
    onResetAllTaken: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Meus Medicamentos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Adicionar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (medications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum medicamento cadastrado ainda.\nClique em 'Adicionar' acima para começar.",
                    fontSize = 18.sp,
                    color = TextLight
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(medications) { medication ->
                    MedicationCardItem(
                        medication = medication,
                        onToggleTaken = { onToggleTaken(medication.id, medication.isTaken == 0L) },
                        onDelete = { onDeleteMedication(medication.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onResetAllTaken,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reiniciar Status do Dia (Desmarcar Todos)", fontSize = 15.sp, color = TextSecondary)
            }
        }
    }

    if (showAddDialog) {
        AddMedicationDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, dosage, frequency, reminder_time, notes ->
                onAddMedication(name, dosage, frequency, reminder_time, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun MedicationCardItem(
    medication: Medications,
    onToggleTaken: () -> Unit,
    onDelete: () -> Unit
) {
    val isTaken = medication.isTaken != 0L

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 3.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isTaken) SecondaryLight else SurfaceCard
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onToggleTaken,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (isTaken) Icons.Default.CheckCircle else Icons.Default.Clear,
                    contentDescription = if (isTaken) "Tomado" else "Pendente",
                    tint = if (isTaken) SecondaryGreen else TextLight,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medication.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Dose: ${medication.dosage}  |  Horário: ${medication.reminder_time}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryDark
                )
                Text(
                    text = "Frequência: ${medication.frequency}",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                if (!medication.notes.isNullOrBlank()) {
                    Text(
                        text = "Obs: ${medication.notes}",
                        fontSize = 13.sp,
                        color = TextLight
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Deletar",
                    tint = RedAlert
                )
            }
        }
    }
}

@Composable
fun AddMedicationDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, dosage: String, frequency: String, reminder_time: String, notes: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var reminder_time by remember { mutableStateOf("08:00") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Novo Medicamento",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Medicamento") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosagem (ex: 1 comprimido, 5ml)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = reminder_time,
                    onValueChange = { reminder_time = it },
                    label = { Text("Horário (ex: 08:00)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Frequência (ex: 1x ao dia, De 8 em 8h)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Observações (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && dosage.isNotBlank()) {
                        onConfirm(name, dosage, frequency, reminder_time, notes.ifBlank { null })
                    }
                },
                enabled = name.isNotBlank() && dosage.isNotBlank(),
                colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryBlue),
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
