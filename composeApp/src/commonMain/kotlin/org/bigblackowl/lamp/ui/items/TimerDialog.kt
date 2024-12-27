package org.bigblackowl.lamp.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.window.Dialog
import org.bigblackowl.lamp.data.Timer
import org.bigblackowl.lamp.data.util.getCurrentTime

@Composable
fun TimerDialog(
    isTimerSetOn: Boolean,
    onDismissRequest: () -> Unit,
    onSetTimerClicked: (Timer) -> Unit,
    onCancelTimerClicked: () -> Unit
) {
    val currentTime = getCurrentTime()  // Получаем текущее время
    println("TimerDialog time: $currentTime")

    println("TimerDialog hour: ${currentTime.hour}, minute: ${currentTime.minute}")
    Dialog(onDismissRequest = onDismissRequest) {
        var timerHour by remember { mutableStateOf(currentTime.hour) }  // Устанавливаем текущее время в часах
        var timerMinute by remember { mutableStateOf(currentTime.minute) }  // Устанавливаем текущее время в минутах
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Set Time", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(20.dp))

                // Часы
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            timerHour = (timerHour - 1).coerceIn(0, 23)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("-", color = Color.White)
                    }

                    Text(
                        text = "$timerHour",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = {
                            timerHour = (timerHour + 1).coerceIn(0, 23)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("+", color = Color.White)
                    }
                }

                // Минуты
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            timerMinute = (timerMinute - 1).coerceIn(0, 59)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("-", color = Color.White)
                    }

                    Text(
                        text = "$timerMinute",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = {
                            timerMinute = (timerMinute + 1).coerceIn(0, 59)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("+", color = Color.White)
                    }
                }

                if (isTimerSetOn) {
                    Button(
                        onClick = onCancelTimerClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text("Turn off", color = Color.White)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Save button
                    Button(
                        onClick = {
                            onSetTimerClicked(Timer(timerHour, timerMinute))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text("Save", color = Color.White)
                    }

                    // Dismiss button
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            }
        }
    }
}
