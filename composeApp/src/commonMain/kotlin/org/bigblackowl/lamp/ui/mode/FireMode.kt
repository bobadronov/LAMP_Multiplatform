package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.bigblackowl.lamp.ui.items.sliders.BrightnessSlider

@Composable
fun FireMode(
    commonBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit,
) {

    Column(
        Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BrightnessSlider(
            commonBrightness = commonBrightness,
            onBrightnessChange = onBrightnessChange,
            onBrightnessChangeFinished = onBrightnessChangeFinished
        )
    }
}