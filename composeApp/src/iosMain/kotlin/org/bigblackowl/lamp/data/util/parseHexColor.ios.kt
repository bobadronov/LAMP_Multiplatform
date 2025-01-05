package org.bigblackowl.lamp.data.util

import androidx.compose.ui.graphics.Color

actual fun parseHexColor(hex: String): Color {

    return try {
        val rgba = hex.removePrefix("#").chunked(2).map { it.toInt(16) }
        val red = rgba[0] / 255f
        val green = rgba[1] / 255f
        val blue = rgba[2] / 255f
        val alpha = if (rgba.size > 3) rgba[3] / 255f else 1f
        Color(red, green, blue, alpha)
    } catch (e: Exception) {
        Color.Gray // Default color in case of an error
    }

}