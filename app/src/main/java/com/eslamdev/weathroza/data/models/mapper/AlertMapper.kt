package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import java.util.Calendar

object AlertMapper {

    fun create(
        name: String,
        parameter: WeatherParameter,
        threshold: Float,
        isAbove: Boolean,
        frequency: AlertFrequency,
        startHour: Int?,
        startMinute: Int?,
        endHour: Int?,
        endMinute: Int?,
    ): AlertEntity {
        val startMillis = toMillis(startHour, startMinute)
        val endMillis = toMillis(endHour, endMinute)

        return AlertEntity(
            name = name,
            parameter = parameter,
            threshold = threshold,
            isAbove = isAbove,
            frequency = frequency,
            startTimeMillis = startMillis,
            endTimeMillis = endMillis,
        )
    }

    private fun toMillis(hour: Int?, minute: Int?): Long? {
        if (hour == null || minute == null) return null
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }.timeInMillis
    }
}