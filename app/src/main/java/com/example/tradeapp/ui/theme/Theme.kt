package com.example.tradeapp.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class GlassCustomColors(
    val backgroundGradientStart: Color = GlassDeepBlue,
    val backgroundGradientMiddle: Color = GlassNavy,
    val backgroundGradientEnd: Color = GlassDarkBlue,
    val backgroundCirclePink: Color = GlassPink,
    val backgroundCircleBlue: Color = GlassDarkBlue,
    val cardBackground: Color = Color.White,
    val cardBorder: Color = Color.White,
    val textFieldIndicator: Color = GlassPink,
    val pullToRefreshContainer: Color = GlassNavy,
    val pullToRefreshContent: Color = GlassPink
)

val LocalGlassCustomColors = staticCompositionLocalOf { GlassCustomColors() }

val MaterialTheme.glassColors: GlassCustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalGlassCustomColors.current

private val DarkColorScheme = darkColorScheme(
    primary = GlassPink,
    secondary = GlassBlue,
    tertiary = GlassGreen,
    background = GlassDeepBlue,
    surface = GlassNavy,
    error = GlassPink,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color.White.copy(alpha = 0.6f),
    surfaceTint = GlassGreen,
    inverseSurface = GlassNavy
)

private val LightColorScheme = DarkColorScheme // Liquid Glass is inherently dark themed

@Composable
fun TradeAppTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
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

    CompositionLocalProvider(LocalGlassCustomColors provides GlassCustomColors()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
