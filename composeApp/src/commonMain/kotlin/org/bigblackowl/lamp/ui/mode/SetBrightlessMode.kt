package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SetBrightlessMode(
    commonBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit,

    ) {
    Column(
        Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Text("Brightness:", color = Color.White)
        Slider(
            value = commonBrightness, valueRange = 1f..255f, // Значения диапазона
//            steps = 8,
            onValueChange = onBrightnessChange,
            onValueChangeFinished = onBrightnessChangeFinished
        )
    }
}