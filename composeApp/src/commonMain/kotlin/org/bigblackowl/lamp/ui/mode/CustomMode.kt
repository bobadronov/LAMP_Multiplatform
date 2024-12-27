package org.bigblackowl.lamp.ui.mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import org.bigblackowl.lamp.data.parseHexColor
import org.bigblackowl.lamp.data.util.toHexString
import org.bigblackowl.lamp.ui.theme.DefaultColorList


@Composable
fun CustomMode(numLeds: Int, onValueChange: (List<String>) -> Unit) {
    // Состояние выбранного цвета
    var selectedColor by remember { mutableStateOf(Color.Black) }
    // Массив текущих цветов светодиодов
    var ledColors by remember { mutableStateOf(List(numLeds) { "#000000" }) }

    // Количество ячеек (10 частей)
    val numSections = 10
    val ledsPerSection = kotlin.math.ceil(numLeds.toDouble() / numSections).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Выбор цвета из предопределённого списка
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            items(DefaultColorList.size) { index ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { selectedColor = DefaultColorList[index] }
                        .background(
                            DefaultColorList[index],
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            2.dp,
                            color = if (selectedColor == DefaultColorList[index]) Color.White else Color.Unspecified,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Отображение 10 ячеек для групп светодиодов
        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            items(numSections) { sectionIndex ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(30.dp)
                        .background(
                            parseHexColor(
                                if (sectionIndex * ledsPerSection < ledColors.size)
                                    ledColors[sectionIndex * ledsPerSection]
                                else "#000000"
                            )
                        )
                        .clickable {
                            ledColors = ledColors.toMutableList().apply {
                                for (i in 0 until ledsPerSection) {
                                    if (sectionIndex * ledsPerSection + i < this.size) {
                                        this[sectionIndex * ledsPerSection + i] = "#000000"
                                    }
                                }
                                if (sectionIndex * ledsPerSection < this.size) {
                                    this[sectionIndex * ledsPerSection] = selectedColor.toHexString()
                                }
                            }
                            onValueChange(ledColors)
                        }
                        .border(
                            1.dp,
                            color = Color.DarkGray,
//                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            ledColors = List(numLeds) { "#000000" }  // All LEDs reset to black
            onValueChange(ledColors)  // Update the external state
        }) {

                Text("Clear")
        }
    }
}
