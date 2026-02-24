package com.eslamdev.weathroza.data.datasources.remote

import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    fun getWeather(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units
    ): Flow<Result<WeatherEntity>>

    fun getHourlyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units,
        count: Int = 24
    ): Flow<Result<List<HourlyForecastEntity>>>

    fun getDailyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units,
        count: Int = 7
    ): Flow<Result<List<DailyForecastEntity>>>

    fun getPossibleCities(
        cityName: String,
        limit: Int = 7
    ): Flow<Result<List<CityEntity>>>

    fun getCityNamesLocalized(
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Flow<Result<List<CityEntity>>>
}