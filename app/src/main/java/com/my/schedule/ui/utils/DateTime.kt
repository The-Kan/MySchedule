package com.my.schedule.ui.utils

import java.time.format.DateTimeFormatter

class DateTime {
    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT)
    }
}