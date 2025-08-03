package com.bekircaglar.wepick.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


data class WePickColors(
    val primary: Color,
    val darkPrimary: Color,
    val white: Color,
    val black: Color,
    val gray: Color,
    val lightGray: Color,
    val lighterGray: Color,
    val darkGray: Color,
    val surfaceGray: Color,
    val orange: Color,
    val darkBlue: Color,
    val lightBlue: Color,
    val navyBlue: Color,
    val yellow: Color,
    val darkGreen: Color
)

data class AppColors(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onBackground: Color,
    val onSurface: Color
)

private val LightWePickColors = AppColors(
    primary = Color(0xFF238AFF),        // Logo'daki parlak mavi (üstteki kart)
    primaryVariant = Color(0xFF90C5FF), // Logo'daki açık mavi (alttaki kart)
    secondary = Color(0xFF03DAC6),     // İkinci renk olarak yeşil
    onSecondary = Color(0xFF000000),   // Yeşil üzeri
    background = Color(0xFFf5f5f5),     // Logo arkaplanı (beyaza yakın, #F8F9FA)
    surface = Color(0xFFFFFFFF),        // Saf beyaz yüzeyler
    onPrimary = Color(0xFFFFFFFF),      // Mavi üzeri beyaz yazı
    onBackground = Color(0xFF222222),   // Açık tema için koyu yazı
    onSurface = Color(0xFF222222)
)

// DarkTheme renk şeması
private val DarkWePickColors = AppColors(
    primary = Color(0xFF238AFF),        // Logo'daki parlak mavi (değişmeden)
    primaryVariant = Color(0xFF4885C7), // Açık maviye daha koyu alternatif (#4885C7)
    secondary = Color(0xFF03DAC6),     // İkinci renk olarak yeşil (değişmeden)
    onSecondary = Color(0xFFFFFFFF),   // Yeşil üzeri beyaz
    background = Color(0xFF2f2f2f),     // Koyu arkaplan
    surface = Color(0xFF000000),        // Koyu yüzeyler
    onPrimary = Color(0xFF222222),      // Mavi üzeri koyu yazı (isteğe bağlı)
    onBackground = Color(0xFFF8F9FA),   // Koyu tema için açık yazı
    onSurface = Color(0xFFF8F9FA)
)

val LocalWePickColors = staticCompositionLocalOf { LightWePickColors }

private val LightColorScheme = lightColorScheme(
    primary =  LightWePickColors.primary,
    onPrimary = LightWePickColors.onPrimary,
    secondary = LightWePickColors.secondary,
    onSecondary = LightWePickColors.onSecondary,
    background = LightWePickColors.background,
    onBackground = LightWePickColors.onBackground,
    surface = LightWePickColors.surface,
    onSurface = LightWePickColors.onSurface,
)

private val DarkColorScheme = lightColorScheme(
    primary =  LightWePickColors.primary,
    onPrimary = LightWePickColors.onPrimary,
    secondary = LightWePickColors.secondary,
    onSecondary = LightWePickColors.onSecondary,
    background = LightWePickColors.background,
    onBackground = LightWePickColors.onBackground,
    surface = LightWePickColors.surface,
    onSurface = LightWePickColors.onSurface,
)

@Composable
fun WePickTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkWePickColors else LightWePickColors
    val materialColors = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalWePickColors provides colors) {
        MaterialTheme(
            colorScheme = materialColors,
            content = content
        )
    }
}


object WePickTheme {
    val colors: AppColors
        @Composable
        get() = LocalWePickColors.current
}