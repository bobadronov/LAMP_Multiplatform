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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
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
import org.bigblackowl.lamp.data.util.parseHexColor
import org.bigblackowl.lamp.data.util.toHexString
import org.bigblackowl.lamp.ui.theme.DefaultColorList


@Composable
fun CustomMode(
    numLeds: Int,
    onValueChange: (List<String>) -> Unit,
    commonBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit,
) {
    // Состояние выбранного цвета
    var selectedColor by remember { mutableStateOf(Color.Black) }
    // Массив текущих цветов светодиодов
    var ledColors by remember { mutableStateOf(List(numLeds) { "#000000" }) }

    // Количество ячеек (например 10 частей)
    val numSections = 10

    // Равномерное деление LED по секциям
    // Округляем в большую сторону, если нужно
    val ledsPerSection =
        if (numLeds <= 10) numLeds else (kotlin.math.ceil(numLeds / numSections.toDouble())).toInt()

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Выбор цвета из предопределённого списка
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

        Spacer(Modifier.height(20.dp))

        // Отображение ячеек для групп светодиодов
        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            items(numSections) { sectionIndex ->
                Box(
                    modifier = Modifier.padding(5.dp).size(40.dp).background(
                        parseHexColor(
                            if (sectionIndex * ledsPerSection < ledColors.size) ledColors[sectionIndex * ledsPerSection]
                            else "#000000"
                        )
                    ).clickable {
                        ledColors = ledColors.toMutableList().apply {
                            // Обновляем все LEDs в группе
                            for (i in 0 until ledsPerSection) {
                                val ledIndex = sectionIndex * ledsPerSection + i
                                if (ledIndex < this.size) {
                                    this[ledIndex] =
                                        selectedColor.toHexString() // Применяем выбранный цвет ко всем диодам секции
                                }
                            }
                        }
                        onValueChange(ledColors)
                    }.border(
                        1.dp,
                        color = Color.DarkGray,
                    )
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("Brightness:", color = Color.White)
        Slider(
            value = commonBrightness,
            valueRange = 1f..255f, // Значения диапазона
//            steps = 8,
            onValueChange = onBrightnessChange,
            onValueChangeFinished = onBrightnessChangeFinished
        )
        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            // Сбросить все светодиоды на черный
            ledColors = List(numLeds) { "#000000" }
            onValueChange(ledColors)  // Обновить внешний стейт
        }) {
            Text("Clear")
        }
    }
}
