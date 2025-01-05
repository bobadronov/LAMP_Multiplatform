package org.bigblackowl.lamp.ui.items.sliders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    enabled: Boolean = true,
    onSpeedValueChange: (Float) -> Unit,
    onSpeedValueChangeFinished: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Speed:", color = Color.White)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text("Fast", color = Color.White)

            Slider(
                value = value,
                enabled = enabled,
                valueRange = 5f..150f,
                onValueChange = onSpeedValueChange,
                onValueChangeFinished = onSpeedValueChangeFinished,
                modifier = Modifier.weight(1f)
            )

            Text("Slow", color = Color.White)
        }
    }
}