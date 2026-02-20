package com.eslamdev.weathroza.data.datasources.remote

import com.eslamdev.weathroza.BuildConfig
import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.data.config.network.RetrofitHelper
import com.eslamdev.weathroza.data.datasources.remote.service.WeatherService
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.mapper.DailyForecastMapper
import com.eslamdev.weathroza.data.models.mapper.HourlyForecastMapper
import com.eslamdev.weathroza.data.models.mapper.WeatherMapper
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

class WeatherRemoteDataSource {
    private val weatherService: WeatherService = RetrofitHelper.weatherService

    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC
    ): WeatherEntity {
        val weatherDto = weatherService.getWeather(
            apiKey = BuildConfig.WEATHER_API_KEY,
            latitude = latitude,
            longitude = longitude,
            language = language.code,
            units = units.value
        )
        return WeatherMapper.toEntity(weatherDto)
    }

    suspend fun getHourlyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC,
        count: Int = 24
    ): List<HourlyForecastEntity> {
        val response = weatherService.getHourlyForecast(
            apiKey = BuildConfig.WEATHER_API_KEY,
            latitude = latitude,
            longitude = longitude,
            language = language.code,
            units = units.value,
            count = count
        )
        return HourlyForecastMapper.toEntityList(response)
    }

    suspend fun getDailyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC,
        count: Int = 7
    ): List<DailyForecastEntity> {
        val response = weatherService.getDailyForecast(
            apiKey = BuildConfig.WEATHER_API_KEY,
            latitude = latitude,
            longitude = longitude,
            language = language.code,
            units = units.value,
            count = count
        )
        return DailyForecastMapper.toEntityList(response)
    }
}