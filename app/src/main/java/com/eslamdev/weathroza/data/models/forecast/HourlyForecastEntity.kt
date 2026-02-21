package com.eslamdev.weathroza.data.models.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_forecast", primaryKeys = ["cityId", "dt"])
data class HourlyForecastEntity(
    val dt: Long,
    val temp: Double,
    val icon: String,
    val iconResId: Int = 0,
    val iconUrl: String = "",
    val cityId:String
)
