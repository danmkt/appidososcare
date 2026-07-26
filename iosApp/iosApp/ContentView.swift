import SwiftUI
import shared
import KMPNativeCoroutinesAsync

@MainActor
class ContentViewModel: ObservableObject {
    private let viewModel: SharedViewModel
    private let notifier: Notifier
    
    @Published var medications: [Medications] = []
    @Published var totalWaterMl: Int = 0
    
    init() {
        let notifier = Notifier()
        self.viewModel = SharedViewModel(
            database: AppDatabase(driver: DatabaseDriverFactory().createDriver()),
            notifier: notifier,
            coroutineScope: CoroutineScope(context: Dispatchers.Main)
        )
        self.notifier = notifier
        self.notifier.requestAuthorization()
        
        Task {
            let stream = asyncStream(for: viewModel.medications)
            for try await data in stream {
                self.medications = data
            }
        }
        
        Task {
            let stream = asyncStream(for: viewModel.totalWaterMl)
            for try await data in stream {
                self.totalWaterMl = data.intValue
            }
        }
    }
    
    func addWater() {
        viewModel.addWater(amountMl: 250)
    }
    
    func testNotification() {
        notifier.showNotification(title: "Teste iOS", message: "Esta é uma notificação de teste.")
    }
}

struct ContentView: View {
    @StateObject private var viewModel = ContentViewModel()

    var body: some View {
        VStack {
            Text("Remédios: \(viewModel.medications.count)")
            Text("Água: \(viewModel.totalWaterMl) ml")
            Button(action: {
                viewModel.addWater()
            }) {
                Text("Adicionar Água")
            }
            Button(action: {
                viewModel.testNotification()
            }) {
                Text("Testar Notificação")
            }
        }
    }
}
