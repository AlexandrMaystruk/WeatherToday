package com.gmail.maystruks08.whatweathernow.data

import java.text.SimpleDateFormat
import java.util.*

private const val FULL_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
private const val TIME = "HH:mm"
private val calendar = Calendar.getInstance()
private val formatter = SimpleDateFormat(FULL_DATE_TIME, Locale.getDefault())
private val days = arrayOf("Нд", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")


fun String.parseDate(): Date {
    formatter.applyPattern(FULL_DATE_TIME)
    return runCatching { formatter.parse(this) }.getOrNull() ?: Date()
}

fun Date.toHumanDayOfWeek(): String {
    calendar.time = this
    return days[calendar.get(Calendar.DAY_OF_WEEK) - 1]
}

fun Date.getShortTime(): String {
    formatter.applyPattern(TIME)
    return formatter.format(this)
}
