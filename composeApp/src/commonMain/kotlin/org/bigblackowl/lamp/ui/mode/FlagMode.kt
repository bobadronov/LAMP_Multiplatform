package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FlagMode(
    flagIsStatic: Boolean,
    commonBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    flagSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    onSpeedChangeFinished: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Static mode:", color = Color.White)
        Switch(
            checked = flagIsStatic,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.width(60.dp),
            colors = SwitchDefaults.colors()
        )
        Text("Speed:", color = Color.White)
        Slider(
            value = flagSpeed,
            enabled = !flagIsStatic,
            valueRange = 1f..10f, // Значения диапазона
            steps = 8,
            onValueChange = onSpeedChange,
            onValueChangeFinished = onSpeedChangeFinished
        )
        Text("Brightness:", color = Color.White)
        Slider(
            value = commonBrightness,
            valueRange = 1f..255f, // Значения диапазона
//            steps = 8,
            onValueChange = onBrightnessChange,
            onValueChangeFinished = onBrightnessChangeFinished
        )
    }
}