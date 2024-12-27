package org.bigblackowl.lamp.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SliderMode(
    speed: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Speed:", color = Color.White)
        Slider(
            value = speed,
            valueRange = 1f..10f, // Значения диапазона
            steps = 8,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished
        )
    }
}