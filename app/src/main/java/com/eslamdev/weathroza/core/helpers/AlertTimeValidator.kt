package com.eslamdev.weathroza.core.helpers

import java.util.Calendar

object AlertTimeValidator {

    private const val MAX_FUTURE_HOURS = 6

    fun isInFuture(hour: Int, minute: Int): Boolean {
        val now = Calendar.getInstance()
        val picked = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return picked.after(now)
    }

    fun isWithinMaxFuture(hour: Int, minute: Int): Boolean {
        val limit = Calendar.getInstance().apply {
            add(Calendar.HOUR_OF_DAY, MAX_FUTURE_HOURS)
        }
        val picked = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (!after(Calendar.getInstance())) add(Calendar.DAY_OF_MONTH, 1)
        }
        return !picked.after(limit)
    }

    fun isEndAfterStart(
        startHour: Int, startMinute: Int,
        endHour: Int, endMinute: Int,
    ): Boolean {
        val now = Calendar.getInstance()

        val start = Calendar.getInstance().apply {
            set(Calendar.YEAR, now.get(Calendar.YEAR))
            set(Calendar.MONTH, now.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (!after(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val end = Calendar.getInstance().apply {
            set(Calendar.YEAR, now.get(Calendar.YEAR))
            set(Calendar.MONTH, now.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, endHour)
            set(Calendar.MINUTE, endMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (!after(start)) add(Calendar.DAY_OF_MONTH, 1)
        }

        return end.after(start)
    }
}