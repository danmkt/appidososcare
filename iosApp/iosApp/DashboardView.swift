import SwiftUI

struct DashboardView: View {
    @EnvironmentObject var viewModel: ContentViewModel

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    Text("Resumo do Dia")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                        .padding(.horizontal)

                    SummaryCard(
                        title: "Medicamentos",
                        value: "\(viewModel.medications.filter { $0.isTaken != 0 }.count) / \(viewModel.medications.count)",
                        caption: "tomados",
                        color: .blue
                    )

                    SummaryCard(
                        title: "Hidratação",
                        value: "\(viewModel.totalWaterMl) ml",
                        caption: "de 2000 ml",
                        color: .cyan
                    )

                    SummaryCard(
                        title: "Refeições",
                        value: "\(viewModel.mealLogs.count)",
                        caption: "registradas",
                        color: .orange
                    )
                }
                .padding()
            }
            .navigationTitle("Dashboard")
        }
    }
}

struct SummaryCard: View {
    let title: String
    let value: String
    let caption: String
    let color: Color

    var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .font(.headline)
                .foregroundColor(.secondary)
            
            Spacer()
            
            Text(value)
                .font(.system(size: 36, weight: .bold, design: .rounded))
                .foregroundColor(color)
            
            Text(caption)
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding()
        .frame(maxWidth: .infinity, minHeight: 120, alignment: .leading)
        .background(Color(.systemGray6))
        .cornerRadius(20)
    }
}
