package com.eslamdev.weathroza.data.repo

import android.content.Context
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

class WeatherRepo(val context: Context) {
    private val localDataSource = WeatherLocalDataSource(context)
    private val remoteDataSource = WeatherRemoteDataSource()
    
    suspend fun fetchWeatherFromApi(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        units: String = "metric"
    ): WeatherEntity {
        return remoteDataSource.getWeather(
            latitude = latitude,
            longitude = longitude,
            language = language,
            units = units
        )
    }
    
    suspend fun fetchAndSaveWeather(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        units: String = "metric"
    ): WeatherEntity {
        val weatherEntity = remoteDataSource.getWeather(
            latitude = latitude,
            longitude = longitude,
            language = language,
            units = units
        )
        localDataSource.insertWeather(weatherEntity)
        return weatherEntity
    }
    
    suspend fun getWeatherByCity(cityName: String): WeatherEntity? {
        return localDataSource.getWeatherByCity(cityName)
    }
    
    suspend fun getAllWeather(): List<WeatherEntity> {
        return localDataSource.getAllWeather()
    }
    
    suspend fun deleteAllWeather() {
        localDataSource.deleteAllWeather()
    }
    
    suspend fun getWeatherOrFetch(
        cityName: String,
        latitude: Double,
        longitude: Double,
        language: String = "en",
        units: String = "metric"
    ): WeatherEntity {
        val localWeather = getWeatherByCity(cityName)
        return if (localWeather != null) {
            localWeather
        } else {
            fetchAndSaveWeather(latitude, longitude, language, units)
        }
    }
}