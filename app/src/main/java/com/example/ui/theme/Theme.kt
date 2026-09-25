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

private val DarkColorScheme = darkColorScheme(
    primary = GimbiGreenDarkPrimary,
    onPrimary = Color(0xFF00381F),
    primaryContainer = GimbiGreenDark,
    onPrimaryContainer = GimbiGreenContainer,
    secondary = GimbiGold,
    onSecondary = Color(0xFF3B2300),
    secondaryContainer = Color(0xFF553500),
    onSecondaryContainer = GimbiGoldContainer,
    tertiary = GimbiCoffeeLight,
    background = GimbiDarkBackground,
    surface = GimbiDarkSurface,
    surfaceVariant = GimbiDarkSurfaceVariant,
    onBackground = GimbiDarkTextPrimary,
    onSurface = GimbiDarkTextPrimary,
    outline = Color(0xFF88958A)
)

private val LightColorScheme = lightColorScheme(
    primary = GimbiGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GimbiGreenContainer,
    onPrimaryContainer = GimbiOnGreenContainer,
    secondary = GimbiGold,
    onSecondary = Color.White,
    secondaryContainer = GimbiGoldContainer,
    onSecondaryContainer = GimbiOnGoldContainer,
    tertiary = GimbiCoffeeBrown,
    onTertiary = Color.White,
    tertiaryContainer = GimbiCoffeeContainer,
    background = GimbiBackgroundLight,
    surface = GimbiSurfaceLight,
    surfaceVariant = GimbiSurfaceVariant,
    onBackground = GimbiTextPrimary,
    onSurface = GimbiTextPrimary,
    outline = GimbiBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted Gimbi regional palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
