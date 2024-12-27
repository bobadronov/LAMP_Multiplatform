package org.bigblackowl.lamp.data.util

import org.bigblackowl.lamp.data.Timer
import java.time.LocalDate
import java.time.LocalTime

actual fun getCurrentTime(): Timer {
    val time = LocalTime.now()  // Получаем текущее время
    val date = LocalDate.now()  // Получаем текущую дату
    return Timer(
        day = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        hour = time.hour,
        minute = time.minute
    )
}