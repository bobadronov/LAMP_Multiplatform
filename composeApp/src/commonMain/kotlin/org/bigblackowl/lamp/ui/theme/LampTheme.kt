package org.bigblackowl.lamp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun LampTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Choose the color palette based on the current theme mode
    val colors = if (darkTheme) darkColorPalette else lightColorPalette

    // Apply the MaterialTheme with color scheme, typography, and shapes
    MaterialTheme(
        colorScheme = colors,
//        typography = Typography,
        shapes = Shapes,
        content = content
    )
}