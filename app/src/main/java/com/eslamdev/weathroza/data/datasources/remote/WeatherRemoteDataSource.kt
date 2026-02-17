package com.eslamdev.weathroza.data.datasources.remote

import com.eslamdev.weathroza.BuildConfig
import com.eslamdev.weathroza.data.config.network.RetrofitHelper
import com.eslamdev.weathroza.data.models.mapper.WeatherMapper
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

class WeatherRemoteDataSource {
    private val weatherService: WeatherService = RetrofitHelper.weatherService
    
    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        units: String = "metric"
    ): WeatherEntity {
        val weatherDto = weatherService.getWeather(
            apiKey = BuildConfig.WEATHER_API_KEY,
            latitude = latitude,
            longitude = longitude,
            language = language,
            units = units
        )
        return WeatherMapper.toEntity(weatherDto)
    }
}