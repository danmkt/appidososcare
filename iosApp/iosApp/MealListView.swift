import SwiftUI
import shared

struct MealListView: View {
    @EnvironmentObject var viewModel: ContentViewModel
    @State private var showingAddSheet = false

    var body: some View {
        NavigationView {
            List {
                ForEach(viewModel.mealLogs, id: \.id) { meal in
                    HStack {
                        VStack(alignment: .leading) {
                            Text(meal.mealType)
                                .font(.headline)
                            Text(meal.description_)
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                        }
                        Spacer()
                        Text(meal.time)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                }
                .onDelete(perform: deleteMeal)
            }
            .navigationTitle("Refeições")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showingAddSheet = true }) {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showingAddSheet) {
                AddMealView()
            }
        }
    }

    private func deleteMeal(at offsets: IndexSet) {
        offsets.forEach { index in
            let meal = viewModel.mealLogs[index]
            viewModel.deleteMeal(id: meal.id)
        }
    }
}

struct AddMealView: View {
    @EnvironmentObject var viewModel: ContentViewModel
    @Environment(\.dismiss) var dismiss

    @State private var mealType: String = "Almoço"
    @State private var description: String = ""
    @State private var time: String = "12:00"

    var body: some View {
        NavigationView {
            Form {
                Picker("Tipo de Refeição", selection: $mealType) {
                    Text("Café da Manhã").tag("Café da Manhã")
                    Text("Almoço").tag("Almoço")
                    Text("Lanche").tag("Lanche")
                    Text("Jantar").tag("Jantar")
                }
                TextField("Descrição", text: $description)
                TextField("Horário", text: $time)
            }
            .navigationTitle("Nova Refeição")
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Cancelar") {
                        dismiss()
                    }
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Salvar") {
                        viewModel.addMeal(
                            type: mealType,
                            description: description,
                            time: time
                        )
                        dismiss()
                    }
                    .disabled(description.isEmpty)
                }
            }
        }
    }
}
