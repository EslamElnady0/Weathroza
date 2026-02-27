package com.eslamdev.weathroza.data.repo.impl

import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.mapper.FavouriteLocationMapper
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


class WeatherRepoImpl(
    private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val alarmScheduler: AlarmScheduler,
) : WeatherRepo {

    override fun getWeatherFromApi(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units
    ): Flow<Result<WeatherEntity>> =
        remoteDataSource.getWeather(latitude, longitude, language, units)

    override fun refreshHomeData(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units
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

    override suspend fun getCachedHomeData(
        cityId: Long?
    ): Triple<WeatherEntity, List<HourlyForecastEntity>, List<DailyForecastEntity>>? {
        cityId ?: return null
        val weather = localDataSource.getWeatherByCityId(cityId) ?: return null
        val hourly = localDataSource.getHourlyForecastsByCityId(cityId)
        val daily = localDataSource.getDailyForecastsByCityId(cityId)
        return Triple(weather, hourly, daily)
    }

    override suspend fun clearCityData(cityId: Long) {
        localDataSource.deleteWeatherByCityId(cityId)
        localDataSource.deleteHourlyForecastsByCityId(cityId)
        localDataSource.deleteDailyForecastsByCityId(cityId)
    }

    // ── Cities ──────────────────────────────────────────────────

    override fun getPossibleCities(
        cityName: String,
        limit: Int
    ): Flow<Result<List<CityEntity>>> =
        remoteDataSource.getPossibleCities(cityName, limit)

    override fun getCityNamesLocalized(
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Flow<Result<List<CityEntity>>> =
        remoteDataSource.getCityNamesLocalized(latitude, longitude, limit)

    // ── Favourites ──────────────────────────────────────────────

    override fun getAllFavourites(): Flow<Result<List<FavouriteLocationEntity>>> =
        localDataSource.getAllFavourites()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override fun isFavourite(cityId: Long): Flow<Result<Boolean>> =
        localDataSource.isFavourite(cityId)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override suspend fun addFavourite(
        weatherEntity: WeatherEntity,
        latLng: LatLng,
        cityEntity: CityEntity
    ) {
        val favourite = FavouriteLocationMapper.toEntity(weatherEntity, latLng, cityEntity)
        localDataSource.insertFavourite(favourite)
    }

    override suspend fun removeFavourite(cityId: Long) {
        localDataSource.deleteFavouriteById(cityId)
        clearCityData(cityId)
    }

    override suspend fun refreshFavouriteWeather(cityId: Long, temp: Double, iconUrl: String) {
        localDataSource.updateLastTemp(cityId = cityId, temp = temp, iconUrl = iconUrl)
    }

    // ── Alert ──────────────────────────────────────────────

    override fun getScheduledAlerts(): Flow<Result<List<AlertEntity>>> =
        localDataSource.getScheduledAlerts()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override fun getWeatherAlerts(): Flow<Result<List<AlertEntity>>> =
        localDataSource.getWeatherAlerts()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override fun getAllAlerts(): Flow<Result<List<AlertEntity>>> =
        localDataSource.getAllAlerts()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override fun getAlertById(id: Long): Flow<Result<AlertEntity?>> =
        localDataSource.getAlertById(id)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override suspend fun insertAlert(alert: AlertEntity): Long =
        localDataSource.insertAlert(alert)

    override suspend fun toggleAlert(id: Long, isEnabled: Boolean) =
        localDataSource.updateEnabled(id, isEnabled)

    override suspend fun deleteAlert(id: Long) =
        localDataSource.deleteAlert(id)

    override suspend fun insertOneTimeAlert(alert: AlertEntity): Long {
        val id = localDataSource.insertAlert(alert)
        val alertWithId = alert.copy(id = id)
        alarmScheduler.scheduleAlert(alertWithId)
        return id
    }

    override suspend fun cancelOneTimeAlert(alertId: Long) {
        alarmScheduler.cancelAlert(alertId)
        localDataSource.updateEnabled(alertId, false)
        localDataSource.deleteAlert(alertId)
    }

}