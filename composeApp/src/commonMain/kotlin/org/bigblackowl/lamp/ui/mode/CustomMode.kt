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
import androidx.compose.ui.unit.dp
import org.bigblackowl.lamp.data.util.parseHexColor
import org.bigblackowl.lamp.data.util.toHexString
import org.bigblackowl.lamp.ui.theme.DefaultColorList

@Composable
fun CustomMode(
    colorList: List<String>,
    onValueChange: (List<String>) -> Unit,
    commonBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit,
) {
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var ledColors by remember { mutableStateOf(List(10) { "#000000" }) }
    val squareSize = 50.dp

    LaunchedEffect(colorList) {
        ledColors = colorList
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Color selection
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
        // Draw 10 squares with Canvas
        // Draw 10 squares with Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            // Коррекция позиции относительно центра
                            val totalWidth = (squareSize.toPx() + 5.dp.toPx()) * 10 - 5.dp.toPx()
                            val startX = (size.width - totalWidth) / 2
                            val tappedIndex =
                                ((offset.x - startX) / (squareSize.toPx() + 5.dp.toPx())).toInt()

                            if (tappedIndex in 0 until 10) {
                                val newColors = ledColors.toMutableList()
                                newColors[tappedIndex] = selectedColor.toHexString()
                                ledColors = newColors
                                onValueChange(ledColors)
                            }
                        },
                        onDoubleTap = { offset ->
                            // Коррекция позиции относительно центра
                            val totalWidth = (squareSize.toPx() + 5.dp.toPx()) * 10 - 5.dp.toPx()
                            val startX = (size.width - totalWidth) / 2
                            val tappedIndex =
                                ((offset.x - startX) / (squareSize.toPx() + 5.dp.toPx())).toInt()

                            if (tappedIndex in 0 until 10) {
                                val newColors = ledColors.toMutableList()
                                newColors[tappedIndex] = "#000000" // Установить черный цвет
                                ledColors = newColors
                                onValueChange(ledColors)
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        // Коррекция позиции относительно центра
                        val totalWidth = (squareSize.toPx() + 5.dp.toPx()) * 10 - 5.dp.toPx()
                        val startX = (size.width - totalWidth) / 2
                        val startIndex =
                            ((change.previousPosition.x - startX) / (squareSize.toPx() + 5.dp.toPx())).toInt()
                        val endIndex =
                            ((change.position.x - startX) / (squareSize.toPx() + 5.dp.toPx())).toInt()

                        if (dragAmount != 0f) {
                            val range = if (dragAmount > 0) {
                                startIndex..endIndex
                            } else {
                                endIndex..startIndex
                            }

                            val newColors = ledColors.toMutableList()
                            for (index in range) {
                                if (index in 0 until 10) {
                                    newColors[index] = selectedColor.toHexString()
                                }
                            }
                            ledColors = newColors
                            onValueChange(ledColors)
                        }
                        change.consume()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
                val totalWidth = (squareSize.toPx() + 5.dp.toPx()) * 10 - 5.dp.toPx()
                val startX = (size.width - totalWidth) / 2

                // Draw each square manually
                drawRect(
                    color = parseHexColor(ledColors.getOrElse(0) { "#000000" }),
                    topLeft = Offset(startX, 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(1) { "#000000" }),
                    topLeft = Offset(startX + squareSize.toPx() + 5.dp.toPx(), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(2) { "#000000" }),
                    topLeft = Offset(startX + 2 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(3) { "#000000" }),
                    topLeft = Offset(startX + 3 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(4) { "#000000" }),
                    topLeft = Offset(startX + 4 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(5) { "#000000" }),
                    topLeft = Offset(startX + 5 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(6) { "#000000" }),
                    topLeft = Offset(startX + 6 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(7) { "#000000" }),
                    topLeft = Offset(startX + 7 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(8) { "#000000" }),
                    topLeft = Offset(startX + 8 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )

                drawRect(
                    color = parseHexColor(ledColors.getOrElse(9) { "#000000" }),
                    topLeft = Offset(startX + 9 * (squareSize.toPx() + 5.dp.toPx()), 0f),
                    size = Size(squareSize.toPx(), squareSize.toPx())
                )
            }
        }


        Spacer(Modifier.height(30.dp))

        // Brightness control
        Text("Brightness:", color = Color.White)
        Slider(
            value = commonBrightness,
            valueRange = 10f..255f,
            onValueChange = onBrightnessChange,
            onValueChangeFinished = onBrightnessChangeFinished
        )

        Spacer(Modifier.height(20.dp))

        // Clear button
        Button(onClick = {
            ledColors = List(10) { "#000000" }
            onValueChange(ledColors)
        }) {
            Text("Clear", color = Color.Black)
        }
    }
}
