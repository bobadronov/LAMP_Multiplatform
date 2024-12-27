package org.bigblackowl.lamp.data.util

import org.bigblackowl.lamp.data.Timer

actual fun getCurrentTime(): Timer {
    val time = java.time.LocalTime.now()  // Получаем текущее время
    return Timer(time.hour, time.minute)
}