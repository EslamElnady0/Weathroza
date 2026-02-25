package com.eslamdev.weathroza.data.config.db

import androidx.room.TypeConverter
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter


class AlertConverters {
    @TypeConverter
    fun toWeatherParameter(value: String) = WeatherParameter.valueOf(value)
    @TypeConverter
    fun fromWeatherParameter(value: WeatherParameter) = value.name

    @TypeConverter
    fun toAlertFrequency(value: String) = AlertFrequency.valueOf(value)
    @TypeConverter
    fun fromAlertFrequency(value: AlertFrequency) = value.name
}