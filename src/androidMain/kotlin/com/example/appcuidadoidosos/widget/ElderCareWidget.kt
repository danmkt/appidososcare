package com.example.appcuidadoidosos.widget

import android.content.Context
import androidx.compose.runtime.Composable
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
import com.example.appcuidadoidosos.ui.theme.PrimaryBlue

class ElderCareWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }

    @Composable
    private fun WidgetContent() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(PrimaryBlue)
                .padding(16.dp)
        ) {
            Text(text = "Resumo do Cuidado")
            // TODO: Adicionar dados reais do banco de dados
            Text(text = "Água: 1000/2000 ml")
            Text(text = "Remédios: 2/5 tomados")
        }
    }
}
