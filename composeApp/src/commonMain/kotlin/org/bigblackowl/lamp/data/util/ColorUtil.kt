package org.bigblackowl.lamp.data.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// Extension function to convert Int to Hex String
fun Int.toHexString(): String {
    return StringBuilder().apply {
        append("#")
        append((this@toHexString shr 16 and 0xFF).toString(16).padStart(2, '0'))
        append((this@toHexString shr 8 and 0xFF).toString(16).padStart(2, '0'))
        append((this@toHexString and 0xFF).toString(16).padStart(2, '0'))
    }.toString().uppercase()
}

// Extension function for Color to get Hex string
fun Color.toHexString(): String {
    return this.toArgb().toHexString()
}