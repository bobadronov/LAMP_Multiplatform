package org.bigblackowl.lamp.ui.items.sliders

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
fun SpeedSlider(
    value: Float,
    onSpeedValueChange: (Float) -> Unit,
    onSpeedValueChangeFinished: () -> Unit
) {

    Column(
        Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Speed:", color = Color.White)
        Slider(
            value = value,
            valueRange = 5f..150f,
            onValueChange = onSpeedValueChange,
            onValueChangeFinished = onSpeedValueChangeFinished
        )
    }
}