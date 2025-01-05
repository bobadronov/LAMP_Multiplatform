package org.bigblackowl.lamp.ui.items.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var timerDay by remember { mutableStateOf(currentTime.day) }
    var timerMonth by remember { mutableStateOf(currentTime.month) }
    var timerYear by remember { mutableStateOf(currentTime.year) }
    var timerHour by remember { mutableStateOf(currentTime.hour) }
    var timerMinute by remember { mutableStateOf(currentTime.minute) }

    // Логика для корректировки времени, дней, месяцев и лет
    fun adjustTime() {
        // Проверка минут
        if (timerMinute >= 60) {
            val additionalHours = timerMinute / 60
            timerMinute = timerMinute % 60
            timerHour += additionalHours
        } else if (timerMinute < 0) {
            val additionalHours = (timerMinute / 60) - 1
            timerMinute = 60 + (timerMinute % 60)
            timerHour += additionalHours
        }

        // Проверка часов
        if (timerHour >= 24) {
            val additionalDays = timerHour / 24
            timerHour = timerHour % 24
            timerDay += additionalDays
        } else if (timerHour < 0) {
            val additionalDays = (timerHour / 24) - 1
            timerHour = 24 + (timerHour % 24)
            timerDay += additionalDays
        }

        // Проверка дней в месяце
        val daysInMonth = getDaysInMonth(timerMonth, timerYear)
        if (timerDay > daysInMonth) {
            timerDay = 1
            timerMonth += 1
        } else if (timerDay < 1) {
            timerMonth -= 1
            if (timerMonth < 1) {
                timerMonth = 12
                timerYear -= 1
            }
            timerDay = getDaysInMonth(timerMonth, timerYear)
        }

        // Проверка месяца
        if (timerMonth > 12) {
            timerMonth = 1
            timerYear += 1
        } else if (timerMonth < 1) {
            timerMonth = 12
            timerYear -= 1
        }
    }


    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray, contentColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Timer", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Часы
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                timerHour += 1
                                adjustTime() // Корректируем время после изменения
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("+", color = Color.White)
                        }

                        AnimatedContent(
                            targetState = timerHour,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { it } + fadeIn())
                                        .togetherWith(slideOutVertically { -it } + fadeOut())
                                } else {
                                    (slideInVertically { -it } + fadeIn())
                                        .togetherWith(slideOutVertically { it } + fadeOut())
                                }
                            }
                        ) { targetHour ->
                            Text(
                                text = "$targetHour".padStart(2,'0'),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Button(
                            onClick = {
                                timerHour -= 1
                                adjustTime() // Корректируем время после изменения
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("-", color = Color.White)
                        }
                    }

                    Text(
                        text = ":",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    // Минуты
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                timerMinute += 1
                                adjustTime() // Корректируем время после изменения
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("+", color = Color.White)
                        }

                        AnimatedContent(
                            targetState = timerMinute,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { it } + fadeIn())
                                        .togetherWith(slideOutVertically { -it } + fadeOut())
                                } else {
                                    (slideInVertically { -it } + fadeIn())
                                        .togetherWith(slideOutVertically { it } + fadeOut())
                                }
                            }
                        ) { targetMinute ->
                            Text(
                                text = "$targetMinute".padStart(
                                    2,
                                    '0'
                                ),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Button(
                            onClick = {
                                timerMinute -= 1
                                adjustTime() // Корректируем время после изменения
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("-", color = Color.White)
                        }

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Форматирование даты в виде "День.Месяц.Год"
                Text("At date: ${timerDay}.${timerMonth}.${timerYear}")
                Spacer(modifier = Modifier.height(10.dp))
                // Кнопки для добавления 5, 15 и 30 минут
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        timerMinute += 5
                        adjustTime() // Корректируем время после изменения
                    }) {
                        Text("+5 minutes", color = Color.White)
                    }

                    TextButton(onClick = {
                        timerMinute += 15
                        adjustTime() // Корректируем время после изменения
                    }) {
                        Text("+15 minutes", color = Color.White)
                    }

                    TextButton(onClick = {
                        timerMinute += 30
                        adjustTime() // Корректируем время после изменения
                    }) {
                        Text("+30 minutes", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (isTimerSetOn) {
                    Button(
                        onClick = onCancelTimerClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text("Turn off", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Save button
                    Button(
                        onClick = {
                            onSetTimerClicked(
                                Timer(
                                    day = timerDay,
                                    month = timerMonth,
                                    year = timerYear,
                                    hour = timerHour,
                                    minute = timerMinute
                                )
                            )
                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text("Save", color = Color.Black)
                    }

                    // Dismiss button
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Dismiss", color = Color.Black)
                    }
                }
            }
        }
    }
}

private fun getDaysInMonth(month: Int, year: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31 // Январь, Март, Май, Июль, Август, Октябрь, Декабрь
        4, 6, 9, 11 -> 30 // Апрель, Июнь, Сентябрь, Ноябрь
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28 // Февраль
        else -> 31 // На всякий случай, если месяц некорректен
    }
}
