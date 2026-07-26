import SwiftUI
import shared
import KMPNativeCoroutinesAsync

@MainActor
class ContentViewModel: ObservableObject {
    private let sharedViewModel: SharedViewModel
    
    @Published var medications: [Medications] = []
    @Published var waterLogs: [Water_logs] = []
    @Published var mealLogs: [Meal_logs] = []
    @Published var totalWaterMl: Int = 0
    
    init() {
        let notifier = IosNotifier()
        self.sharedViewModel = SharedViewModel(
            database: AppDatabase(driver: DatabaseDriverFactory().createDriver()),
            notifier: notifier,
            coroutineScope: CoroutineScope(context: Dispatchers.Main)
        )
        notifier.requestAuthorization()
        
        Task {
            for try await meds in asyncStream(for: sharedViewModel.medications) {
                self.medications = meds
            }
        }
        Task {
            for try await logs in asyncStream(for: sharedViewModel.waterLogs) {
                self.waterLogs = logs
            }
        }
        Task {
            for try await logs in asyncStream(for: sharedViewModel.mealLogs) {
                self.mealLogs = logs
            }
        }
        Task {
            for try await total in asyncStream(for: sharedViewModel.totalWaterMl) {
                self.totalWaterMl = total.intValue
            }
        }
    }
    
    func addMedication(name: String, dosage: String, frequency: String, reminder_time: String, notes: String?) {
        sharedViewModel.addMedication(name: name, dosage: dosage, frequency: frequency, reminder_time: reminder_time, notes: notes)
    }
    
    func deleteMedication(id: Int64) {
        sharedViewModel.deleteMedication(id: id)
    }
    
    func addWater(amount: Int32) {
        sharedViewModel.addWater(amountMl: amount)
    }
    
    func deleteWater(id: Int64) {
        sharedViewModel.deleteWaterLog(id: id)
    }
    
    func addMeal(type: String, description: String, time: String) {
        sharedViewModel.addMeal(mealType: type, description: description, time: time)
    }
    
    func deleteMeal(id: Int64) {
        sharedViewModel.deleteMeal(id: id)
    }
}

struct ContentView: View {
    @StateObject private var viewModel = ContentViewModel()

    var body: some View {
        TabView {
            DashboardView()
                .tabItem {
                    Label("Resumo", systemImage: "house.fill")
                }
            
            MedicationListView()
                .tabItem {
                    Label("Remédios", systemImage: "pill.fill")
                }
            
            WaterListView()
                .tabItem {
                    Label("Água", systemImage: "drop.fill")
                }
            
            MealListView()
                .tabItem {
                    Label("Refeições", systemImage: "fork.knife")
                }
        }
        .environmentObject(viewModel)
    }
}
