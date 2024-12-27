package org.bigblackowl.lamp.data.util

import org.bigblackowl.lamp.data.Timer
import java.time.LocalTime

actual fun getCurrentTime(): Timer {
    val time = LocalTime.now()  // Получаем текущее время
    return Timer(time.hour, time.minute)
}