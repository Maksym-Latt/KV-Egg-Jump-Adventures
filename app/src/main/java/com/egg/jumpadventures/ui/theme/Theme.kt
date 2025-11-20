package com.egg.jumpadventures.ui.theme

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

private val WarmLightColorScheme = lightColorScheme(
    primary = Color(0xFFFFC66E),
    onPrimary = Color(0xFF4B2C07),
    secondary = Color(0xFFFFE59D),
    onSecondary = Color(0xFF4B2C07),
    tertiary = Color(0xFFFFB26B),
    background = Color(0xFFFFF4D9),
    surface = Color(0xFFFFF0CA),
    onBackground = Color(0xFF4B2C07),
    onSurface = Color(0xFF4B2C07)
)

private val WarmDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC66E),
    onPrimary = Color(0xFF2B1502),
    secondary = Color(0xFFFFD586),
    onSecondary = Color(0xFF2B1502),
    tertiary = Color(0xFFFF9F68),
    background = Color(0xFF2A1B0E),
    surface = Color(0xFF3A2815),
    onBackground = Color(0xFFFFE7C3),
    onSurface = Color(0xFFFFE7C3)
)

@Composable
fun EggJumpAdventures(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> WarmDarkColorScheme
        else -> WarmLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
