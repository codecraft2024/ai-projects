package com.ghosttalk.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val GhostLightColorScheme = lightColorScheme(
    primary = GhostViolet,
    onPrimary = GhostSurfaceLight,
    primaryContainer = GhostSentBubbleLight,
    onPrimaryContainer = GhostOnSentLight,
    secondary = GhostIndigo,
    onSecondary = GhostSurfaceLight,
    background = GhostBackgroundLight,
    onBackground = GhostOnReceivedLight,
    surface = GhostSurfaceLight,
    onSurface = GhostOnReceivedLight,
    surfaceVariant = GhostSentBubbleLight,
    onSurfaceVariant = GhostOnReceivedLight,
    error = GhostError,
    outline = GhostVioletLight.copy(alpha = 0.4f)
)

private val GhostDarkColorScheme = darkColorScheme(
    primary = GhostVioletLight,
    onPrimary = GhostBackgroundDark,
    primaryContainer = GhostSentBubbleDark,
    onPrimaryContainer = GhostOnSentDark,
    secondary = GhostVioletLight,
    onSecondary = GhostBackgroundDark,
    background = GhostBackgroundDark,
    onBackground = GhostOnReceivedDark,
    surface = GhostSurfaceDark,
    onSurface = GhostOnReceivedDark,
    surfaceVariant = GhostReceivedBubbleDark,
    onSurfaceVariant = GhostOnReceivedDark,
    error = GhostError,
    outline = GhostVioletLight.copy(alpha = 0.3f)
)

@Composable
fun GhostTalkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> GhostDarkColorScheme
        else -> GhostLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GhostTypography,
        shapes = GhostShapes,
        content = content
    )
}
