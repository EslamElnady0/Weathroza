package com.eslamdev.weathroza.data.models.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_forecast")
data class HourlyForecastEntity(
    @PrimaryKey
    val dt: Long,
    val temp: Double,
    val icon: String
)
