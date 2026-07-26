package com.example.appcuidadoidosos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = PrimaryBlue,
    primaryVariant = PrimaryDark,
    secondary = SecondaryGreen,
    background = BackgroundLight,
    surface = SurfaceCard,
    onPrimary = SurfaceCard,
    onSecondary = SurfaceCard,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun ElderCareTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        content = content
    )
}
