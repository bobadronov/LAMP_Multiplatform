package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.bigblackowl.lamp.data.util.toHexString
import org.bigblackowl.lamp.ui.items.sliders.BrightnessSlider
import org.bigblackowl.lamp.ui.theme.DefaultColorList

@Composable
fun GradientMode(
    onValueChange: (List<String>) -> Unit,
    commonBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit
) {
    var startColor by remember { mutableStateOf(Color.Black) }
    var endColor by remember { mutableStateOf(Color.Black) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Select Start Color
        Text("Select Start Color:", color = Color.White)
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            items(DefaultColorList.size) { index ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { startColor = DefaultColorList[index] }
                        .background(
                            DefaultColorList[index], shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            2.dp,
                            color = if (startColor == DefaultColorList[index]) Color.White else Color.Unspecified,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }
        HorizontalDivider()
        // Select End Color
        Text("Select End Color:", color = Color.White)
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            items(DefaultColorList.size) { index ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { endColor = DefaultColorList[index] }
                        .background(
                            DefaultColorList[index], shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            2.dp,
                            color = if (endColor == DefaultColorList[index]) Color.White else Color.Unspecified,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Clear Gradient Button
            Button(
                onClick = {
                    onValueChange(listOf(Color.Black.toHexString(), Color.Black.toHexString()))
                },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text("Clear", color = Color.Black)
            }
            // Apply Gradient Button
            Button(
                onClick = {
                    onValueChange(listOf(startColor.toHexString(), endColor.toHexString()))
                },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text("Apply Gradient", color = Color.Black)
            }
        }

        BrightnessSlider(
            commonBrightness = commonBrightness,
            onBrightnessChange = onBrightnessChange,
            onBrightnessChangeFinished = onBrightnessChangeFinished
        )
    }
}
