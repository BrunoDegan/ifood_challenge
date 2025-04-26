package com.brunodegan.ifood_challenge.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primaryContainer = Color(0xFF00BCD4),
    secondaryContainer = Color(0xFF3F51B5),
    primary = Color.Transparent,
    onPrimary = Color.White,
    secondary = Color.White,
    onSecondary = Color.LightGray,
    tertiary = Color.White,
    onTertiary = Color.White,
    background = Color.Transparent,
    onSurface = Color(0xFF03A9F4),
    surface = Color(0xFF3F51B5),
    onError = Color.Transparent
)

private val LightColorScheme = lightColorScheme(
    primaryContainer = Color(0xFF00BCD4),
    secondaryContainer = Color(0xFF3F51B5),
    primary = Color.Transparent,
    onPrimary = Color.White,
    secondary = Color.LightGray,
    onSecondary = Color.LightGray,
    tertiary = Color.Black,
    onTertiary = Color.White,
    background = Color.Transparent,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF03A9F4),
    surface = Color(0xFF3F51B5),
    onError = Color.Transparent
)

@Composable
fun IfoodChallengeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val shapes = Shapes(
        extraSmall = MaterialTheme.shapes.extraSmall,
        small = MaterialTheme.shapes.small,
        medium = MaterialTheme.shapes.medium,
        large = MaterialTheme.shapes.large,
        extraLarge = MaterialTheme.shapes.extraLarge,
    )

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
        shapes = shapes,
        typography = Typography,
        content = content
    )
}