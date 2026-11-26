package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class ThemeMode(val label: String) {
    SYSTEM("सिस्टम थीम"),
    DARK("डार्क"),
    LIGHT("लाइट")
}

private val HpsDarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color(0xFF032318),
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = EmeraldSoft,
    secondary = CyanBright,
    onSecondary = Color(0xFF042638),
    secondaryContainer = CyanContainerDark,
    onSecondaryContainer = CyanContainerLight,
    tertiary = AmberTertiary,
    onTertiary = Color(0xFF2B1800),
    tertiaryContainer = AmberDark,
    onTertiaryContainer = AmberLight,
    error = CoralDanger,
    onError = Color.White,
    errorContainer = CoralContainerDark,
    onErrorContainer = CoralContainerLight,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerLowest = DarkBackground,
    surfaceContainerLow = DarkSurface,
    surfaceContainer = DarkSurfaceElevated,
    surfaceContainerHigh = DarkSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = Color(0xFF243044)
)

private val HpsLightColorScheme = lightColorScheme(
    primary = EmeraldDeep,
    onPrimary = Color.White,
    primaryContainer = EmeraldSoft,
    onPrimaryContainer = Color(0xFF022C22),
    secondary = CyanSecondary,
    onSecondary = Color.White,
    secondaryContainer = CyanContainerLight,
    onSecondaryContainer = Color(0xFF082F49),
    tertiary = AmberTertiary,
    onTertiary = Color.White,
    tertiaryContainer = AmberLight,
    onTertiaryContainer = Color(0xFF451A03),
    error = CoralDanger,
    onError = Color.White,
    errorContainer = CoralContainerLight,
    onErrorContainer = Color(0xFF450A0A),
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerLowest = LightSurface,
    surfaceContainerLow = LightSurfaceElevated,
    surfaceContainer = Color(0xFFF1F5F9),
    surfaceContainerHigh = LightSurfaceVariant,
    outline = LightOutline,
    outlineVariant = Color(0xFFE2E8F0)
)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun HumanPerformanceTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    val colorScheme = if (isDark) HpsDarkColorScheme else HpsLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
