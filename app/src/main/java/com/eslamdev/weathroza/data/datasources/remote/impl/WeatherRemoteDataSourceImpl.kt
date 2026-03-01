package com.eslamdev.weathroza.data.datasources.remote.impl

import com.eslamdev.weathroza.BuildConfig
import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource
import com.eslamdev.weathroza.data.datasources.remote.service.WeatherService
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.mapper.CityMapper
import com.eslamdev.weathroza.data.models.mapper.DailyForecastMapper
import com.eslamdev.weathroza.data.models.mapper.HourlyForecastMapper
import com.eslamdev.weathroza.data.models.mapper.WeatherMapper
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl(
    private val weatherService: WeatherService
) : WeatherRemoteDataSource {

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units
    ): Flow<Result<WeatherEntity>> =
        flow {
            val response = weatherService.getWeather(
                apiKey = BuildConfig.WEATHER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                language = language.code,
                units = units.value
            )
            emit(Result.success(WeatherMapper.toEntity(response)))
        }.catch {
            emit(Result.failure(it))
        }

    override fun getHourlyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units,
        count: Int
    ): Flow<Result<List<HourlyForecastEntity>>> =
        flow {
            val response = weatherService.getHourlyForecast(
                apiKey = BuildConfig.WEATHER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                language = language.code,
                units = units.value,
                count = count
            )
            emit(Result.success(HourlyForecastMapper.toEntityList(response)))
        }.catch {
            emit(Result.failure(it))
        }

    override fun getDailyForecast(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units,
        count: Int
    ): Flow<Result<List<DailyForecastEntity>>> =
        flow {
            val response = weatherService.getDailyForecast(
                apiKey = BuildConfig.WEATHER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                language = language.code,
                units = units.value,
                count = count
            )
            emit(Result.success(DailyForecastMapper.toEntityList(response)))
        }.catch {
            emit(Result.failure(it))
        }

    override fun getPossibleCities(
        cityName: String,
        limit: Int
    ): Flow<Result<List<CityEntity>>> =
        flow {
            val response = weatherService.getPossibleCities(
                apiKey = BuildConfig.WEATHER_API_KEY,
                cityName = cityName,
                limit = limit
            )
            emit(Result.success(CityMapper.toEntityList(response)))
        }.catch {
            emit(Result.failure(it))
        }

    override fun getCityNamesLocalized(
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Flow<Result<List<CityEntity>>> =
        flow {
            val response = weatherService.getCityNamesLocalized(
                apiKey = BuildConfig.WEATHER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
            emit(Result.success(CityMapper.toEntityList(response)))
        }.catch {
            emit(Result.failure(it))
        }
}