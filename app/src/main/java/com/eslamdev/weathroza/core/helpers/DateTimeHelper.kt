package com.eslamdev.weathroza.core.helpers

import com.eslamdev.weathroza.core.langmanager.LanguageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeHelper {

    fun formatUnixTimestamp(
        timestamp: Long,
        pattern: String = "hh:mm a",
        locale: Locale = LanguageManager.currentLocale
    ): String {
        val date = Date(timestamp * 1000)
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }

    fun formatTime(timestamp: Long) =
        formatUnixTimestamp(timestamp, "hh:mm a")

    fun formatDate(timestamp: Long) =
        formatUnixTimestamp(timestamp, "EEEE, MMM dd")

    fun formatShortDate(timestamp: Long) =
        formatUnixTimestamp(timestamp, "MMM dd")

    fun formatDayName(timestamp: Long) =
        formatUnixTimestamp(timestamp, "EEEE")

    fun formatHour(timestamp: Long) =
        formatUnixTimestamp(timestamp, "hh a")

    fun formatFullDateTime(timestamp: Long): String {
        val date = formatDate(timestamp)
        val time = formatTime(timestamp)
        return "$date • $time"
    }
}