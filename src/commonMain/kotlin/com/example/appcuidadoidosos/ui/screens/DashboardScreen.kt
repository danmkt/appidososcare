package com.example.appcuidadoidosos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcuidadoidosos.ui.theme.*

@Composable
fun DashboardScreen(
    waterTotalMl: Long,
    waterGoalMl: Long,
    medicationsTakenCount: Int,
    medicationsTotalCount: Int,
    mealsLoggedCount: Int,
    onNavigateToWater: () -> Unit,
    onNavigateToMedications: () -> Unit,
    onNavigateToMeals: () -> Unit
) {
    val waterProgress = if (waterGoalMl > 0) (waterTotalMl.toFloat() / waterGoalMl.toFloat()).coerceIn(0f, 1f) else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabeçalho de Boas-Vindas
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = PrimaryLight
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Resumo do Dia",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Acompanhe seus cuidados de hoje com facilidade.",
                        fontSize = 16.sp,
                        color = TextSecondary
                    )
                }
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Card de Hidratação Rápida
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 3.dp,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = SurfaceCard
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(WaterBlueBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = WaterBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Ingestão de Água",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "$waterTotalMl / $waterGoalMl ml",
                                fontSize = 16.sp,
                                color = WaterBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Button(
                        onClick = onNavigateToWater,
                        colors = ButtonDefaults.buttonColors(backgroundColor = WaterBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ver", color = Color.White, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = waterProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = WaterBlue,
                    backgroundColor = WaterBlueBg
                )
            }
        }

        // Card de Medicamentos
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 3.dp,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = SurfaceCard
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Medicamentos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "$medicationsTakenCount de $medicationsTotalCount tomados",
                            fontSize = 15.sp,
                            color = if (medicationsTakenCount == medicationsTotalCount && medicationsTotalCount > 0) SecondaryGreen else TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Button(
                    onClick = onNavigateToMedications,
                    colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Gerenciar", color = Color.White, fontSize = 15.sp)
                }
            }
        }

        // Card de Alimentação
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 3.dp,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = SurfaceCard
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentOrangeLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null,
                            tint = AccentOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Refeições Hoje",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "$mealsLoggedCount refeições registradas",
                            fontSize = 15.sp,
                            color = TextSecondary
                        )
                    }
                }
                Button(
                    onClick = onNavigateToMeals,
                    colors = ButtonDefaults.buttonColors(backgroundColor = AccentOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Refeições", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}
