package org.bigblackowl.lamp.data.util

import org.bigblackowl.lamp.data.Timer
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate


actual fun getCurrentTime(): Timer {
    val calendar = NSCalendar.currentCalendar
    val date = NSDate()
    val components = calendar.components(
        NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                NSCalendarUnitHour or NSCalendarUnitMinute,
        fromDate = date
    )

    return Timer(
        day = components.day.toInt(),
        month = components.month.toInt(),
        year = components.year.toInt(),
        hour = components.hour.toInt(),
        minute = components.minute.toInt()
    )
}