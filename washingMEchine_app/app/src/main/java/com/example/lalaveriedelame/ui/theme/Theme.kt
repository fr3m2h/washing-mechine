package com.example.lalaveriedelame.ui.theme

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

// Schéma de couleurs pour le mode sombre
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00FF00),
    secondary =Color(0xFF32CD32),
    tertiary = Color(0xFF32CD32),
    background= Color(0xFFFFFFFF),
    onBackground=Color(0xFF000000),
    onPrimary=Color(0xFFDDE0E3),
    surface = Color(0xFF4A5A6A),
    onSurface = Color(0xFFDDE0E3)
)

// Schéma de couleurs pour le mode clair
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00FF00),
    secondary =Color(0xFF32CD32),
    tertiary = Color(0xFF32CD32),
    background= Color(0xFFFFFFFF),
    onBackground=Color(0xFF000000),
    onPrimary=Color(0xFFDDE0E3),
    surface = Color(0xFF4A5A6A),
    onSurface = Color(0xFFDDE0E3)
)

@Composable
fun LalaverieDeLaMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Appliquer les couleurs dynamiques pour Android 12+ si activé
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
        typography = AppTypography, // Typographie personnalisée importée de Typography.kt
        content = content
    )
}
