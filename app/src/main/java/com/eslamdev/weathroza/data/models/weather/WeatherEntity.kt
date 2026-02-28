package com.eslamdev.weathroza.data.models.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val id: Int,
    val name: String = "",
    val weatherMain: String = "",
    val weatherDescription: String = "",
    val weatherIcon: String = "",
    val temp: Double = 0.0,
    val feelsLike: Double = 0.0,
    val tempMin: Double = 0.0,
    val tempMax: Double = 0.0,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val seaLevel: Int = 0,
    val grndLevel: Int = 0,
    val windSpeed: Double = 0.0,
    val windDeg: Int = 0,
    val windGust: Double = 0.0,
    val cloudsAll: Int = 0,
    val country: String = "",
    val sunrise: Long = 0L,
    val sunset: Long = 0L,
    val dt: Long = 0L,
    val iconUrl: String = "",
)