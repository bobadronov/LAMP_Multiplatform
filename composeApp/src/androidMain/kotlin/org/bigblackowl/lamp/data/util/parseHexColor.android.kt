package org.bigblackowl.lamp.data.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

actual fun parseHexColor(hex: String): Color {
    return if (hex.isNotEmpty() && hex.startsWith("#")) {
        try {
            // Try to parse the color
            val androidColor = android.graphics.Color.parseColor(hex)
            Color(androidColor.red / 255f, androidColor.green / 255f, androidColor.blue / 255f, androidColor.alpha / 255f)
        } catch (e: IllegalArgumentException) {
            // Handle invalid color format
            Color.Gray // Default color if parsing fails
        }
    } else {
        // Handle empty or invalid hex
        Color.Gray // Default color if the hex is empty or invalid
    }
}