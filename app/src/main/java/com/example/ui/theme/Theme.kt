package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    primaryContainer = StudioSurfaceElevated,
    onPrimaryContainer = NeonCyan,
    secondary = NeonPink,
    onSecondary = Color.White,
    secondaryContainer = StudioSurfaceDark,
    onSecondaryContainer = Color.White,
    tertiary = NeonCyan,
    onTertiary = StudioDarkBg,
    background = StudioDarkBg,
    onBackground = TextPrimary,
    surface = StudioSurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = StudioSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = StudioCardBorder,
    outlineVariant = Color(0xFF382F6B)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
