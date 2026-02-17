package com.eslamdev.weathroza.core.helpers

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeHelper {
    
    /**
     * Converts Unix timestamp (seconds since epoch) to formatted date string
     * @param timestamp Unix timestamp in seconds
     * @param pattern Date format pattern (e.g., "hh:mm a", "EEE, MMM dd", "dd/MM/yyyy")
     * @param locale Locale for formatting (default is system locale)
     * @return Formatted date string
     */
    fun formatUnixTimestamp(
        timestamp: Long,
        pattern: String = "hh:mm a",
        locale: Locale = Locale.getDefault()
    ): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }
    
    /**
     * Converts Unix timestamp to time string (e.g., "06:45 AM")
     * @param timestamp Unix timestamp in seconds
     * @param locale Locale for formatting (default is system locale)
     * @return Time string in 12-hour format
     */
    fun formatTime(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        return formatUnixTimestamp(timestamp, "hh:mm a", locale)
    }
    
    /**
     * Converts Unix timestamp to date string (e.g., "Monday, Oct 23")
     * @param timestamp Unix timestamp in seconds
     * @param locale Locale for formatting (default is system locale)
     * @return Date string with day name and short month
     */
    fun formatDate(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        return formatUnixTimestamp(timestamp, "EEEE, MMM dd", locale)
    }
    
    /**
     * Converts Unix timestamp to short date string (e.g., "Oct 23")
     * @param timestamp Unix timestamp in seconds
     * @param locale Locale for formatting (default is system locale)
     * @return Short date string
     */
    fun formatShortDate(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        return formatUnixTimestamp(timestamp, "MMM dd", locale)
    }
    
    /**
     * Converts Unix timestamp to day name (e.g., "Monday")
     * @param timestamp Unix timestamp in seconds
     * @param locale Locale for formatting (default is system locale)
     * @return Day name
     */
    fun formatDayName(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        return formatUnixTimestamp(timestamp, "EEEE", locale)
    }
    
    /**
     * Converts Unix timestamp to hour string (e.g., "10 AM")
     * @param timestamp Unix timestamp in seconds
     * @param locale Locale for formatting (default is system locale)
     * @return Hour string in 12-hour format
     */
    fun formatHour(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        return formatUnixTimestamp(timestamp, "hh a", locale)
    }
    
    /**
     * Converts Unix timestamp to full date and time string (e.g., "Monday, Oct 23 • 10:00 AM")
     * @param timestamp Unix timestamp in seconds
     * @param locale Locale for formatting (default is system locale)
     * @return Full date and time string
     */
    fun formatFullDateTime(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        val date = formatDate(timestamp, locale)
        val time = formatTime(timestamp, locale)
        return "$date • $time"
    }
}
