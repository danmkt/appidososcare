package com.example.appcuidadoidosos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appcuidadoidosos.database.AppDatabase
import com.example.appcuidadoidosos.database.Medications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun App(database: AppDatabase) {
    val medicationQueries = database.medicationsQueries
    val coroutineScope = rememberCoroutineScope()

    var medications by remember { mutableStateOf(emptyList<Medications>()) }
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }

    fun loadMedications() {
        coroutineScope.launch {
            val loadedMedications = withContext(Dispatchers.IO) {
                medicationQueries.selectAllMedications().executeAsList()
            }
            medications = loadedMedications
        }
    }

    fun addMedication() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                medicationQueries.insertMedication(name, dosage, frequency, null)
            }
            loadMedications()
            name = ""
            dosage = ""
            frequency = ""
        }
    }

    fun deleteMedication(id: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                medicationQueries.deleteMedicationById(id)
            }
            loadMedications()
        }
    }

    LaunchedEffect(Unit) {
        loadMedications()
    }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Controle de Medicamentos") }) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Formulário para adicionar medicamentos
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome do Medicamento") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("Dosagem (ex: 1 comprimido)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = { frequency = it },
                        label = { Text("Frequência (ex: 2x ao dia)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = ::addMedication,
                        modifier = Modifier.align(Alignment.End),
                        enabled = name.isNotBlank() && dosage.isNotBlank() && frequency.isNotBlank()
                    ) {
                        Text("Adicionar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // Lista de medicamentos
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(medications) { medication ->
                        MedicationItem(
                            medication = medication,
                            onDelete = { deleteMedication(medication.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationItem(medication: Medications, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = medication.name, style = MaterialTheme.typography.h6)
                Text(text = "Dosagem: ${medication.dosage}", style = MaterialTheme.typography.body1)
                Text(text = "Frequência: ${medication.frequency}", style = MaterialTheme.typography.body1)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar Medicamento")
            }
        }
    }
}
