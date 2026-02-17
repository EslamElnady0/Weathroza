package com.eslamdev.weathroza.data.models.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val id: Int,
    
    val name: String,
    
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int,
    val grndLevel: Int,
    
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    
    val cloudsAll: Int,
    
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    
    val dt: Long
)
