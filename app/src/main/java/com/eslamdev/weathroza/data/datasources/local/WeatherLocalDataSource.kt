package com.eslamdev.weathroza.data.datasources.local

import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    // ── Weather ──────────────────────────────────────────────────

    suspend fun insertWeather(weather: WeatherEntity)
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?
    suspend fun getAllWeather(): List<WeatherEntity>
    suspend fun deleteAllWeather()
    suspend fun getWeatherByCityId(cityId: Long): WeatherEntity?
    suspend fun deleteWeatherByCityId(cityId: Long)

    // ── Hourly Forecast ──────────────────────────────────────────

    suspend fun insertHourlyForecasts(forecasts: List<HourlyForecastEntity>)
    suspend fun getHourlyForecasts(): List<HourlyForecastEntity>
    suspend fun deleteAllHourlyForecasts()
    suspend fun getHourlyForecastsByCityId(cityId: Long): List<HourlyForecastEntity>
    suspend fun deleteHourlyForecastsByCityId(cityId: Long)

    // ── Daily Forecast ───────────────────────────────────────────

    suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>)
    suspend fun deleteAllDailyForecasts()
    suspend fun getDailyForecastsByCityId(cityId: Long): List<DailyForecastEntity>
    suspend fun deleteDailyForecastsByCityId(cityId: Long)

    // ── Favourites ───────────────────────────────────────────────

    fun getAllFavourites(): Flow<List<FavouriteLocationEntity>>
    fun getFavouriteById(cityId: Long): Flow<FavouriteLocationEntity?>
    fun isFavourite(cityId: Long): Flow<Boolean>
    suspend fun insertFavourite(favourite: FavouriteLocationEntity)
    suspend fun deleteFavouriteById(cityId: Long)
    suspend fun updateLastTemp(cityId: Long, temp: Double, iconUrl: String)

    // ── Alerts ───────────────────────────────────────────────
    fun getAllAlerts(): Flow<List<AlertEntity>>
    fun getScheduledAlerts(): Flow<List<AlertEntity>>
    fun getWeatherAlerts(): Flow<List<AlertEntity>>
    fun getAlertById(id: Long): Flow<AlertEntity?>
    suspend fun insertAlert(alert: AlertEntity): Long
    suspend fun updateEnabled(id: Long, isEnabled: Boolean)
    suspend fun deleteAlert(id: Long)
    suspend fun deleteAllAlerts()
    suspend fun updateAlertStartTime(alertId: Long, newStartMillis: Long)
}

