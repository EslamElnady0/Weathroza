package com.eslamdev.weathroza.core.helpers

import android.icu.text.SimpleDateFormat
import android.icu.util.ULocale
import java.util.Date
import java.util.Locale

object DateTimeHelper {

    fun formatUnixTimestamp(
        timestamp: Long,
        pattern: String = "hh:mm a",
        locale: Locale
    ): String {
        val date = Date(timestamp * 1000)
        val formatter = SimpleDateFormat(pattern, ULocale.forLocale(locale))
        return formatter.format(date)
    }

    fun formatTime(timestamp: Long, locale: Locale) =
        formatUnixTimestamp(timestamp, "hh:mm a", locale)

    fun formatShortDate(timestamp: Long, locale: Locale) =
        formatUnixTimestamp(timestamp, "MMM dd", locale)

    fun formatDayName(timestamp: Long, locale: Locale) =
        formatUnixTimestamp(timestamp, "EEEE", locale)

    fun formatHour(timestamp: Long, locale: Locale) =
        formatUnixTimestamp(timestamp, "hh a", locale)

    fun formatFullDateTime(timestamp: Long, locale: Locale): String {
        val date = formatUnixTimestamp(timestamp, "EEEE, MMM dd", locale)
        val time = formatUnixTimestamp(timestamp, "hh:mm a", locale)
        return "$date • $time"
    }
}