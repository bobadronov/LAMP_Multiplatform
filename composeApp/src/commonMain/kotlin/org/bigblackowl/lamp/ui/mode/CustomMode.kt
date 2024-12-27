package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import org.bigblackowl.lamp.data.util.parseHexColor
import org.bigblackowl.lamp.data.util.toHexString
import org.bigblackowl.lamp.ui.theme.DefaultColorList

@Composable
fun CustomMode(
    numLeds: Int, // 5...600
    colorList: List<String>,
    onValueChange: (List<String>) -> Unit,
    commonBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit,
) {
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var ledColors by remember { mutableStateOf(List(1900) { "#000000" }) }
    val numSections = 10
    val ledsPerSection = numLeds / numSections
    val squareSize = 38.dp

    LaunchedEffect(colorList) {
        ledColors = colorList
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Select Color
        // (Use same color selection grid as before)
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
                        .clickable { selectedColor = DefaultColorList[index] }
                        .background(
                            DefaultColorList[index], shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            2.dp,
                            color = if (selectedColor == DefaultColorList[index]) Color.White else Color.Unspecified,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        // Draw 10 squares with Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount > 0) {
                            // Swipe Right (Change color to selectedColor)
                            val newColors = ledColors.toMutableList()
                            val sectionIndex =
                                (change.position.x / (squareSize.toPx() + 5.dp.toPx())).toInt()
                            if (sectionIndex in 0 until numSections) {
                                for (i in 0 until ledsPerSection) {
                                    val ledIndex = sectionIndex * ledsPerSection + i
                                    if (ledIndex < newColors.size) {
                                        newColors[ledIndex] = selectedColor.toHexString()
                                    }
                                }
                            }
                            ledColors = newColors
                            onValueChange(ledColors)
                        }
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            // Detect tap on each square
                            val tappedIndex = (offset.x / (squareSize.toPx() + 5.dp.toPx())).toInt()
                            if (tappedIndex in 0 until numSections) {
                                val newColors = ledColors.toMutableList()
                                for (i in 0 until ledsPerSection) {
                                    val ledIndex = tappedIndex * ledsPerSection + i
                                    if (ledIndex < newColors.size) {
                                        newColors[ledIndex] = selectedColor.toHexString()
                                    }
                                }
                                ledColors = newColors
                                onValueChange(ledColors)
                            }
                        },
                        onDoubleTap = { offset ->
                            // Remove color (set to black)
                            val tappedIndex = (offset.x / (squareSize.toPx() + 5.dp.toPx())).toInt()
                            if (tappedIndex in 0 until numSections) {
                                val newColors = ledColors.toMutableList()
                                for (i in 0 until ledsPerSection) {
                                    val ledIndex = tappedIndex * ledsPerSection + i
                                    if (ledIndex < newColors.size) {
                                        newColors[ledIndex] = "#000000" // Set color to black
                                    }
                                }
                                ledColors = newColors
                                onValueChange(ledColors)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw 10 squares
                for (i in 0 until numSections) {
                    val color = if (i * ledsPerSection < ledColors.size) {
                        parseHexColor(ledColors[i * ledsPerSection])
                    } else {
                        Color.Black
                    }
                    val offset = Offset(
                        x = (i * (squareSize.toPx() + 5.dp.toPx())),
                        y = 0f
                    )
                    drawRect(
                        color = color,
                        topLeft = offset,
                        size = Size(squareSize.toPx(), squareSize.toPx())
                    )
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        // Brightness control
        Text("Brightness:", color = Color.White)
        Slider(
            value = commonBrightness,
            valueRange = 1f..255f,
            onValueChange = onBrightnessChange,
            onValueChangeFinished = onBrightnessChangeFinished
        )

        Spacer(Modifier.height(20.dp))

        // Clear button
        Button(onClick = {
            ledColors = List(numLeds) { "#000000" }
            onValueChange(ledColors)
        }) {
            Text("Clear", color = Color.Black)
        }
    }
}