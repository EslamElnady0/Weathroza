package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import java.util.Calendar

object AlertMapper {
    fun create(
        name: String,
        parameter: WeatherParameter,
        threshold: Float,
        isAbove: Boolean,
        frequency: AlertFrequency,
        settings: UserSettings,
        startHour: Int?,
        startMinute: Int?,
        endHour: Int?,
        endMinute: Int?,
    ): AlertEntity = AlertEntity(
        name = name,
        parameter = parameter,
        threshold = threshold,
        thresholdTempUnit = if (parameter == WeatherParameter.TEMP) settings.temperatureUnit else null,
        thresholdWindUnit = if (parameter == WeatherParameter.WIND) settings.windSpeedUnit else null,
        isAbove = isAbove,
        frequency = frequency,
        startTimeMillis = toMillis(startHour, startMinute),
        endTimeMillis = toMillis(endHour, endMinute),
    )

    private fun toMillis(hour: Int?, minute: Int?): Long? {
        if (hour == null || minute == null) return null
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }
}