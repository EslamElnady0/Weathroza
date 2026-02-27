package com.eslamdev.weathroza.data.config.db

import androidx.room.TypeConverter
import com.eslamdev.weathroza.data.models.alert.AlertDay
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit


class AlertConverters {
    @TypeConverter
    fun toWeatherParameter(value: String) = WeatherParameter.valueOf(value)

    @TypeConverter
    fun fromWeatherParameter(value: WeatherParameter) = value.name

    @TypeConverter
    fun toAlertFrequency(value: String) = AlertFrequency.valueOf(value)

    @TypeConverter
    fun fromAlertFrequency(value: AlertFrequency) = value.name

    @TypeConverter
    fun toTemperatureUnit(value: String?) = value?.let { TemperatureUnit.valueOf(it) }

    @TypeConverter
    fun fromTemperatureUnit(value: TemperatureUnit?) = value?.name

    @TypeConverter
    fun toWindSpeedUnit(value: String?) = value?.let { WindSpeedUnit.valueOf(it) }

    @TypeConverter
    fun fromWindSpeedUnit(value: WindSpeedUnit?) = value?.name

    @TypeConverter
    fun fromAlertDaySet(days: Set<AlertDay>): String =
        days.joinToString(",") { it.name }

    @TypeConverter
    fun toAlertDaySet(value: String): Set<AlertDay> =
        if (value.isBlank()) emptySet()
        else value.split(",").mapNotNull {
            runCatching { AlertDay.valueOf(it) }.getOrNull()
        }.toSet()
}