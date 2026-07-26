package com.example.appcuidadoidosos.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.appcuidadoidosos.ui.theme.PrimaryBlue

class ElderCareWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = WidgetDataRepository(context)
        val data = repository.getWidgetData()

        provideContent {
            WidgetContent(data)
        }
    }

    @Composable
    private fun WidgetContent(data: WidgetData) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(PrimaryBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "Resumo do Cuidado",
                style = TextStyle(color = ColorProvider(Color.White))
            )
            Text(
                text = "Água: ${data.waterTotalMl}/${data.waterGoalMl} ml",
                style = TextStyle(color = ColorProvider(Color.White))
            )
            Text(
                text = "Remédios: ${data.medicationsTaken}/${data.medicationsTotal} tomados",
                style = TextStyle(color = ColorProvider(Color.White))
            )
        }
    }
}
