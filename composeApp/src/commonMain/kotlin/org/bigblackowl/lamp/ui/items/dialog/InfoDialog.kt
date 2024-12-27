package org.bigblackowl.lamp.ui.items.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.bigblackowl.lamp.data.DeviceStatus
import org.bigblackowl.lamp.ui.items.text.TextRow

@Composable
fun InfoDialog(
    deviceStatus: DeviceStatus,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // Заголовок
                Text(
                    text = "Device Information",
                    color = Color.Cyan,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Версия
                TextRow(label = "Version", value = deviceStatus.version)

                // Состояние светодиодов
                TextRow(label = "LED State", value = deviceStatus.ledState.toString())
                TextRow(label = "Number of LEDs", value = deviceStatus.NUM_LEDS.toString())

                // Таймер
                TextRow(label = "Timer Active", value = deviceStatus.timerIsActive.toString())
                TextRow(label = "Timer", value = deviceStatus.timer.toString())

                // Данные сенсоров
                TextRow(label = "Temperature", value = "${deviceStatus.temperature}°C")
                TextRow(label = "Humidity", value = "${deviceStatus.humidity}%")

                // Текущий режим
                TextRow(label = "Current Mode", value = deviceStatus.currentMode.toString())

                // Настройки радуги
                TextRow(label = "Rainbow Static", value = deviceStatus.rainbowIsStatic.toString())
                TextRow(label = "Rainbow Speed", value = deviceStatus.rainbowSpeed.toString())

                // Настройки флага
                TextRow(label = "Flag Static", value = deviceStatus.flagIsStatic.toString())
                TextRow(label = "Flag Speed", value = deviceStatus.flagSpeed.toString())

                // Кнопка "Закрыть"
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.DarkGray
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}