import SwiftUI
import shared

struct MedicationListView: View {
    @EnvironmentObject var viewModel: ContentViewModel
    @State private var showingAddSheet = false

    var body: some View {
        NavigationView {
            List {
                ForEach(viewModel.medications, id: \.id) { medication in
                    HStack {
                        VStack(alignment: .leading) {
                            Text(medication.name)
                                .font(.headline)
                            Text("Dose: \(medication.dosage) | Horário: \(medication.reminder_time)")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                        }
                        Spacer()
                        // TODO: Add toggle button for isTaken
                    }
                }
                .onDelete(perform: deleteMedication)
            }
            .navigationTitle("Medicamentos")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showingAddSheet = true }) {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showingAddSheet) {
                AddMedicationView()
            }
        }
    }

    private func deleteMedication(at offsets: IndexSet) {
        offsets.forEach { index in
            let medication = viewModel.medications[index]
            viewModel.deleteMedication(id: medication.id)
        }
    }
}

struct AddMedicationView: View {
    @EnvironmentObject var viewModel: ContentViewModel
    @Environment(\.dismiss) var dismiss

    @State private var name: String = ""
    @State private var dosage: String = ""
    @State private var reminderTime: String = "08:00"
    @State private var frequency: String = "1x ao dia"
    @State private var notes: String = ""

    var body: some View {
        NavigationView {
            Form {
                TextField("Nome do Medicamento", text: $name)
                TextField("Dosagem (ex: 1 comprimido)", text: $dosage)
                TextField("Horário (ex: 08:00)", text: $reminderTime)
                TextField("Frequência (ex: 1x ao dia)", text: $frequency)
                TextField("Notas (opcional)", text: $notes)
            }
            .navigationTitle("Novo Medicamento")
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Cancelar") {
                        dismiss()
                    }
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Salvar") {
                        viewModel.addMedication(
                            name: name,
                            dosage: dosage,
                            frequency: frequency,
                            reminder_time: reminderTime,
                            notes: notes
                        )
                        dismiss()
                    }
                    .disabled(name.isEmpty || dosage.isEmpty)
                }
            }
        }
    }
}
