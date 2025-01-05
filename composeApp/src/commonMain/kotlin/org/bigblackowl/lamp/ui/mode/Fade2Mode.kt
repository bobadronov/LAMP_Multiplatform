package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.bigblackowl.lamp.data.util.parseStringHexToColor
import org.bigblackowl.lamp.data.util.toHexString
import org.bigblackowl.lamp.ui.items.sliders.SpeedSlider
import org.bigblackowl.lamp.ui.theme.DefaultColorList

@Composable
fun Fade2Mode(
    color1: String,
    color2: String,
    speed: Float,
    onSpeedValueChange: (Float) -> Unit,
    onSpeedValueChangeFinished: () -> Unit,
    onSetColorClicked: (color1: String, color2: String) -> Unit
) {
    var firstColor by remember { mutableStateOf(Color.Yellow) }
    var secondColor by remember { mutableStateOf(Color.Blue) }

    LaunchedEffect(color1, color2) {
        firstColor = parseStringHexToColor(color1)
        secondColor = parseStringHexToColor(color2)
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text("Select first Color:", color = Color.White)

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
                        .clickable { firstColor = DefaultColorList[index] }
                        .background(
                            DefaultColorList[index], shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            2.dp,
                            color = if (firstColor == DefaultColorList[index]) Color.White else Color.Unspecified,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }

        HorizontalDivider()

        Text("Select second Color:", color = Color.White)

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
                        .clickable { secondColor = DefaultColorList[index] }
                        .background(
                            DefaultColorList[index], shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            2.dp,
                            color = if (secondColor == DefaultColorList[index]) Color.White else Color.Unspecified,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }

        SpeedSlider(
            value = speed,
            onSpeedValueChange = onSpeedValueChange,
            onSpeedValueChangeFinished = onSpeedValueChangeFinished
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    onSetColorClicked(Color.Black.toHexString(), Color.Black.toHexString())
                },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text("Clear", color = Color.Black)
            }
            // Apply Gradient Button
            Button(
                onClick = {
                    onSetColorClicked(firstColor.toHexString(), secondColor.toHexString())
                },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text("Apply color", color = Color.Black)
            }
        }
    }
}