package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import org.bigblackowl.lamp.ui.theme.DefaultColorList

@Composable
fun ColorControlMode(
    onColorChanged: (ColorEnvelope) -> Unit,
    onColorSelected: (Color) -> Unit,
    initialColor: Color,
    controller: ColorPickerController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            controller = controller,
            onColorChanged = onColorChanged,
            initialColor = initialColor
        )

        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp), controller = controller
        )
        Spacer(Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
//            contentPadding = PaddingValues(15.dp)
        ) {
            items(DefaultColorList.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(40.dp)
                        .clickable { onColorSelected(DefaultColorList[index]) }
                        .background(
                            DefaultColorList[index],
                            shape = RoundedCornerShape(10.dp)
                        )  // Apply rounded corners
                        .border(
                            2.dp,
                            color = if (initialColor == DefaultColorList[index]) Color.White else Color.Unspecified,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }
    }
}


