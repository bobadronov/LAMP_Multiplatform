package org.bigblackowl.lamp.data.util

import androidx.compose.ui.graphics.Color
import java.awt.Color as AwtColor


actual fun parseStringHexToColor(hex: String): Color {

    return try {
        val awtColor = AwtColor.decode(hex)
        Color(
            awtColor.red / 255f,
            awtColor.green / 255f,
            awtColor.blue / 255f,
            awtColor.alpha / 255f
        )
    } catch (e: NumberFormatException) {
        Color.Gray // Default color in case of an error
    }

}