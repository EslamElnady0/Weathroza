package com.eslamdev.weathroza.data.models.forecast

import androidx.room.Entity

@Entity(tableName = "daily_forecast", primaryKeys = ["cityId", "dt"])
data class DailyForecastEntity(
    val cityId: Long,
    val dt: Long,
    val tempDay: Double,
    val feelsLikeDay: Double,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    
    val formattedDayName: String = "",
    val formattedDate: String = "",
    val formattedTemp: String = "",
    val formattedFeelsLike: String = "",
    val iconResId: Int = 0,
    val iconUrl: String = ""
)
