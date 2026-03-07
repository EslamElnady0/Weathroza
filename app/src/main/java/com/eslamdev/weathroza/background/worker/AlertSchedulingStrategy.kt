package com.eslamdev.weathroza.background.worker

import com.eslamdev.weathroza.data.models.alert.AlertDay
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import java.util.Calendar

object AlertSchedulingStrategy {

    fun nextTriggerMillis(alert: AlertEntity, now: Long = System.currentTimeMillis()): Long? {
        if (alert.repeatDays.isEmpty()) return null

        val startCal = Calendar.getInstance().apply {
            timeInMillis = alert.startTimeMillis ?: return null
        }
        val hour = startCal.get(Calendar.HOUR_OF_DAY)
        val minute = startCal.get(Calendar.MINUTE)

        val sortedDays = alert.repeatDays
            .map { it.toCalendarDayOfWeek() }
            .sorted()

        val nowCal = Calendar.getInstance().apply { timeInMillis = now }
        val todayDow = nowCal.get(Calendar.DAY_OF_WEEK)

        val nextDow = sortedDays.firstOrNull { it > todayDow }
            ?: sortedDays.first()

        val daysUntilNext = if (nextDow > todayDow) {
            nextDow - todayDow
        } else {
            7 - todayDow + nextDow
        }

        return Calendar.getInstance().apply {
            timeInMillis = now
            add(Calendar.DAY_OF_MONTH, daysUntilNext)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun AlertDay.toCalendarDayOfWeek(): Int = when (this) {
        AlertDay.SUN -> Calendar.SUNDAY
        AlertDay.MON -> Calendar.MONDAY
        AlertDay.TUE -> Calendar.TUESDAY
        AlertDay.WED -> Calendar.WEDNESDAY
        AlertDay.THU -> Calendar.THURSDAY
        AlertDay.FRI -> Calendar.FRIDAY
        AlertDay.SAT -> Calendar.SATURDAY
    }
}