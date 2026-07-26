import SwiftUI
import shared

struct WaterListView: View {
    @EnvironmentObject var viewModel: ContentViewModel

    var body: some View {
        NavigationView {
            VStack {
                Text("Total de hoje: \(viewModel.totalWaterMl) ml")
                    .font(.title)
                    .fontWeight(.bold)
                    .padding()

                List {
                    ForEach(viewModel.waterLogs, id: \.id) { log in
                        HStack {
                            Text("\(log.amountMl) ml")
                            Spacer()
                            Text(log.timestamp)
                                .foregroundColor(.secondary)
                        }
                    }
                    .onDelete(perform: deleteWater)
                }
            }
            .navigationTitle("Água")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { viewModel.addWater(amount: 250) }) {
                        Image(systemName: "plus")
                    }
                }
            }
        }
    }

    private func deleteWater(at offsets: IndexSet) {
        offsets.forEach { index in
            let log = viewModel.waterLogs[index]
            viewModel.deleteWater(id: log.id)
        }
    }
}
