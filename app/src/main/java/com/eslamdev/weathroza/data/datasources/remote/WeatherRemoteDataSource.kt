package com.eslamdev.weathroza.data.datasources.remote

import com.eslamdev.weathroza.BuildConfig
import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.core.helpers.asResultFlow
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.data.config.network.RetrofitHelper
import com.eslamdev.weathroza.data.datasources.remote.service.WeatherService
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.mapper.CityMapper
import com.eslamdev.weathroza.data.models.mapper.DailyForecastMapper
import com.eslamdev.weathroza.data.models.mapper.HourlyForecastMapper
import com.eslamdev.weathroza.data.models.mapper.WeatherMapper
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

class WeatherRemoteDataSource {
    private val weatherService: WeatherService = RetrofitHelper.weatherService

    fun getWeather(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC
    ): Flow<Result<WeatherEntity>> =
        suspend {
            WeatherMapper.toEntity(
                weatherService.getWeather(
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    latitude = latitude,
                    longitude = longitude,
                    language = language.code,
                    units = units.value
                )
            )
        }.asResultFlow()

    fun getHourlyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC,
        count: Int = 24
    ): Flow<Result<List<HourlyForecastEntity>>> =
        suspend {
            HourlyForecastMapper.toEntityList(
                weatherService.getHourlyForecast(
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    latitude = latitude,
                    longitude = longitude,
                    language = language.code,
                    units = units.value,
                    count = count
                )
            )
        }.asResultFlow()

    fun getDailyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC,
        count: Int = 7
    ): Flow<Result<List<DailyForecastEntity>>> =
        suspend {
            DailyForecastMapper.toEntityList(
                weatherService.getDailyForecast(
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    latitude = latitude,
                    longitude = longitude,
                    language = language.code,
                    units = units.value,
                    count = count
                )
            )
        }.asResultFlow()

    fun getPossibleCities(
        cityName: String,
        limit: Int = 5
    ): Flow<Result<List<CityEntity>>> =
        suspend {
            CityMapper.toEntityList(
                weatherService.getPossibleCities(
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    cityName = cityName,
                    limit = limit
                )
            )
        }.asResultFlow()
}