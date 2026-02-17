package com.eslamdev.weathroza.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

@Dao
interface WeatherDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)
    
    @Query("SELECT * FROM weather WHERE name = :cityName LIMIT 1")
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?
    
    @Query("SELECT * FROM weather")
    suspend fun getAllWeatherList(): List<WeatherEntity>
    
    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()
}