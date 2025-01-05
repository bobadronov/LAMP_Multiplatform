package org.bigblackowl.lamp.ui.mode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import org.bigblackowl.lamp.ui.items.sliders.SpeedSlider

@Composable
fun MeteorMode(
    color: Color,
    onColorChanged: (ColorEnvelope) -> Unit,
    onColorSelected: (Color) -> Unit,
    controller: ColorPickerController,
    speed: Float,
    onSpeedValueChange: (Float) -> Unit,
    onSpeedValueChangeFinished: () -> Unit
) {
    key(color) {
        ColorControlMode(
            onColorChanged = onColorChanged,
            onColorSelected = onColorSelected,
            initialColor = color,
            controller = controller
        )
    }
    SpeedSlider(
        value = speed,
        onSpeedValueChange = onSpeedValueChange,
        onSpeedValueChangeFinished = onSpeedValueChangeFinished
    )
}