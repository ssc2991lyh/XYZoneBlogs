package com.xyzone.blogs.app.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// XYZone 标志性暖橙
val OrangeMain = Color(0xFFFF9800)
val OrangeLight = Color(0xFFFFB74D)

// 日间模式：白橙
private val LightColorScheme = lightColorScheme(
    primary = OrangeMain,
    onPrimary = Color.White,
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    onSurface = Color(0xFF333333)
)

// 夜间模式：黑橙
private val DarkColorScheme = darkColorScheme(
    primary = OrangeLight,
    onPrimary = Color.Black,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE1E1E1)
)

@Composable
fun XYZoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, content = content)
}
