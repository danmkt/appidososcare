package com.example.appcuidadoidosos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcuidadoidosos.database.Water_logs
import com.example.appcuidadoidosos.ui.theme.*

@Composable
fun WaterScreen(
    waterLogs: List<Water_logs>,
    totalWaterMl: Long,
    dailyGoalMl: Long,
    onAddWater: (amountMl: Int) -> Unit,
    onDeleteWaterLog: (id: Long) -> Unit
) {
    val progress = if (dailyGoalMl > 0) (totalWaterMl.toFloat() / dailyGoalMl.toFloat()).coerceIn(0f, 1f) else 0f
    val percentage = (progress * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Text(
            text = "Controle de Água & Hidratação",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        // Card de Progresso Principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(20.dp),
            backgroundColor = WaterBlueBg
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = WaterBlue,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "$totalWaterMl ml de $dailyGoalMl ml",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = WaterBlue
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = WaterBlue,
                    backgroundColor = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$percentage% da meta diária concluída",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
            }
        }

        // Seção de Registro Rápido
        Text(
            text = "Adicionar Rápido",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickWaterButton(
                title = "+200 ml",
                subtitle = "Copo Pequeno",
                onClick = { onAddWater(200) },
                modifier = Modifier.weight(1f)
            )
            QuickWaterButton(
                title = "+300 ml",
                subtitle = "Caneca",
                onClick = { onAddWater(300) },
                modifier = Modifier.weight(1f)
            )
            QuickWaterButton(
                title = "+500 ml",
                subtitle = "Garrafinha",
                onClick = { onAddWater(500) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Histórico de Hoje
        Text(
            text = "Histórico de Hoje",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        if (waterLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum registro de água hoje.\nClique nos botões acima para registrar!",
                    fontSize = 16.sp,
                    color = TextLight
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(waterLogs) { log ->
                    WaterLogItem(
                        log = log,
                        onDelete = { onDeleteWaterLog(log.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun QuickWaterButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(72.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = WaterBlue),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(subtitle, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
        }
    }
}

@Composable
fun WaterLogItem(
    log: Water_logs,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = SurfaceCard
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = WaterBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${log.amountMl} ml",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = log.timestamp,
                        fontSize = 14.sp,
                        color = TextLight
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = RedAlert)
            }
        }
    }
}
