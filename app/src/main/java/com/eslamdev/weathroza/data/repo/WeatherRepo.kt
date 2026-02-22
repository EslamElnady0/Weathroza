package com.eslamdev.weathroza.data.repo

import android.content.Context
import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class WeatherRepo(val context: Context) {
    private val localDataSource = WeatherLocalDataSource(context)
    private val remoteDataSource = WeatherRemoteDataSource()

    // ── Home ────────────────────────────────────────────────────
    fun getWeatherFromApi(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC
    ): Flow<Result<WeatherEntity>> =
        remoteDataSource.getWeather(latitude, longitude, language, units)

    fun refreshHomeData(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC
    ): Flow<Result<Triple<WeatherEntity, List<HourlyForecastEntity>, List<DailyForecastEntity>>>> =
        combine(
            remoteDataSource.getWeather(latitude, longitude, language, units),
            remoteDataSource.getHourlyForecast(latitude, longitude, language, units),
            remoteDataSource.getDailyForecast(latitude, longitude, language, units)
        ) { weatherResult, hourlyResult, dailyResult ->
            weatherResult.mapCatching { weather ->
                Triple(
                    weather,
                    hourlyResult.getOrThrow(),
                    dailyResult.getOrThrow()
                )
            }
        }
            .onEach { result ->
                result.onSuccess { (weather, hourly, daily) ->
                    localDataSource.insertWeather(weather)
                    localDataSource.insertHourlyForecasts(hourly)
                    if (daily.isNotEmpty()) localDataSource.insertDailyForecasts(daily)
                }
            }

    suspend fun getCachedHomeData(
        cityId: Long?
    ): Triple<WeatherEntity, List<HourlyForecastEntity>, List<DailyForecastEntity>>? {
        cityId ?: return null
        val weather = localDataSource.getWeatherByCityId(cityId) ?: return null
        val hourly = localDataSource.getHourlyForecastsByCityId(cityId)
        val daily = localDataSource.getDailyForecastsByCityId(cityId)
        return Triple(weather, hourly, daily)
    }

    suspend fun clearHomeData(cityId: Long) {
        localDataSource.deleteWeatherByCityId(cityId)
        localDataSource.deleteHourlyForecastsByCityId(cityId)
        localDataSource.deleteDailyForecastsByCityId(cityId)
    }

    // ── Cities ──────────────────────────────────────────────────

    fun getPossibleCities(
        cityName: String,
        limit: Int = 5
    ): Flow<Result<List<CityEntity>>> =
        remoteDataSource.getPossibleCities(cityName, limit)

    // ── Favourites ──────────────────────────────────────────────

    fun getAllFavourites(): Flow<Result<List<FavouriteLocationEntity>>> =
        localDataSource.getAllFavourites()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    fun isFavourite(cityId: Long): Flow<Result<Boolean>> =
        localDataSource.isFavourite(cityId)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    suspend fun addFavourite(favourite: FavouriteLocationEntity) {
        localDataSource.insertFavourite(favourite)
    }

    suspend fun removeFavourite(cityId: Long) {
        localDataSource.deleteFavouriteById(cityId)
    }

    suspend fun toggleFavourite(favourite: FavouriteLocationEntity) {
        val isCurrentlyFav = localDataSource.isFavourite(favourite.cityId).first()
        if (isCurrentlyFav) removeFavourite(favourite.cityId)
        else addFavourite(favourite)
    }

    suspend fun refreshFavouriteWeather(
        cityId: Long,
        lat: Double,
        lng: Double,
        language: AppLanguage
    ) {
        remoteDataSource.getWeather(lat, lng, language)
            .first()
            .onSuccess { entity ->
                localDataSource.updateLastTemp(
                    cityId = cityId,
                    temp = entity.temp,
                    iconUrl = "https://openweathermap.org/img/wn/${entity.iconUrl}@4x.png"
                )
            }
    }
}
