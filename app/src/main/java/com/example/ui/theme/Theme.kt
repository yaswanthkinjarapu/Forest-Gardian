package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ForestDarkColorScheme = darkColorScheme(
    primary = ForestGreenLight,
    onPrimary = ForestDarkCanvas,
    primaryContainer = ForestGreenDark,
    onPrimaryContainer = TextPrimaryDark,
    secondary = ForestGoldSecondary,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3B3310),
    onSecondaryContainer = Color(0xFFFFF1C1),
    tertiary = ForestMintAccent,
    background = ForestDarkCanvas,
    onBackground = TextPrimaryDark,
    surface = ForestDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ForestDarkCard,
    onSurfaceVariant = TextSecondaryDark,
    error = ForestFireRed,
    onError = Color.White,
    outline = ForestDarkBorder
)

private val ForestLightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = ForestLightCard,
    onPrimaryContainer = ForestGreenPrimary,
    secondary = ForestGoldSecondary,
    onSecondary = Color.Black,
    tertiary = ForestGreenLight,
    background = ForestLightCanvas,
    onBackground = TextPrimaryLight,
    surface = ForestLightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = ForestLightCard,
    onSurfaceVariant = TextSecondaryLight,
    error = ForestFireRed,
    onError = Color.White,
    outline = ForestLightBorder
)

@Composable
fun ForestGuardianTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our signature forest brand theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> ForestDarkColorScheme
        else -> ForestLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
