package org.bigblackowl.lamp.data.util

import org.bigblackowl.lamp.data.Timer

actual fun getCurrentTime(): Timer {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "HH:mm:ss"
    formatter.locale = NSLocale.currentLocale // Устанавливаем текущую локаль
    val date = NSDate()
    return Timer(date.hour, date.minute)
}