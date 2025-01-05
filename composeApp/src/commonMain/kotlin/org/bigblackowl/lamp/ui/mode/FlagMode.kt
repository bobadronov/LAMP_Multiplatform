package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.bigblackowl.lamp.ui.items.sliders.BrightnessSlider
import org.bigblackowl.lamp.ui.items.sliders.SpeedSlider

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
        SpeedSlider(
            value = flagSpeed,
            enabled = !flagIsStatic,
            onSpeedValueChange = onSpeedChange,
            onSpeedValueChangeFinished = onSpeedChangeFinished
        )
        BrightnessSlider(
            commonBrightness = commonBrightness,
            onBrightnessChange = onBrightnessChange,
            onBrightnessChangeFinished = onBrightnessChangeFinished
        )
    }
}