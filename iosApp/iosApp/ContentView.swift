import SwiftUI
import shared
import KMPNativeCoroutinesAsync

@MainActor
class ContentViewModel: ObservableObject {
    private let viewModel: SharedViewModel
    
    @Published var medications: [Medications] = []
    @Published var totalWaterMl: Int = 0
    
    init() {
        self.viewModel = SharedViewModel(
            database: AppDatabase(driver: DatabaseDriverFactory().createDriver()),
            coroutineScope: CoroutineScope(context: Dispatchers.Main)
        )
        
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
        }
    }
}
