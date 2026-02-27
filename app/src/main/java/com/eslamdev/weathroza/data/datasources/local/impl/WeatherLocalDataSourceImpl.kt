package com.eslamdev.weathroza.data.datasources.local.impl

import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.local.dao.AlertDao
import com.eslamdev.weathroza.data.datasources.local.dao.DailyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.FavouriteLocationDao
import com.eslamdev.weathroza.data.datasources.local.dao.HourlyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.WeatherDao
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(
    private val weatherDao: WeatherDao,
    private val hourlyForecastDao: HourlyForecastDao,
    private val dailyForecastDao: DailyForecastDao,
    private val favouriteLocationDao: FavouriteLocationDao,
    private val alertDao: AlertDao,
) : WeatherLocalDataSource {

    // ── Weather ──────────────────────────────────────────────────

    override suspend fun insertWeather(weather: WeatherEntity) =
        weatherDao.insertWeather(weather)

    override suspend fun getWeatherByCity(cityName: String): WeatherEntity? =
        weatherDao.getWeatherByCity(cityName)

    override suspend fun getAllWeather(): List<WeatherEntity> =
        weatherDao.getAllWeatherList()

    override suspend fun deleteAllWeather() =
        weatherDao.deleteAllWeather()

    override suspend fun getWeatherByCityId(cityId: Long): WeatherEntity? =
        weatherDao.getWeatherByCityId(cityId)

    override suspend fun deleteWeatherByCityId(cityId: Long) =
        weatherDao.deleteWeatherByCityId(cityId)

    // ── Hourly Forecast ──────────────────────────────────────────

    override suspend fun insertHourlyForecasts(forecasts: List<HourlyForecastEntity>) =
        hourlyForecastDao.insertHourlyForecasts(forecasts)

    override suspend fun getHourlyForecasts(): List<HourlyForecastEntity> =
        hourlyForecastDao.getHourlyForecastsList()

    override suspend fun deleteAllHourlyForecasts() =
        hourlyForecastDao.deleteAllHourlyForecasts()

    override suspend fun getHourlyForecastsByCityId(cityId: Long): List<HourlyForecastEntity> =
        hourlyForecastDao.getHourlyForecastsByCityId(cityId)

    override suspend fun deleteHourlyForecastsByCityId(cityId: Long) =
        hourlyForecastDao.deleteHourlyForecastsByCityId(cityId)

    // ── Daily Forecast ───────────────────────────────────────────

    override suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>) =
        dailyForecastDao.insertDailyForecasts(forecasts)

    override suspend fun deleteAllDailyForecasts() =
        dailyForecastDao.deleteAllDailyForecasts()

    override suspend fun getDailyForecastsByCityId(cityId: Long): List<DailyForecastEntity> =
        dailyForecastDao.getDailyForecastsList(cityId)

    override suspend fun deleteDailyForecastsByCityId(cityId: Long) =
        dailyForecastDao.deleteDailyForecastsByCity(cityId)

    // ── Favourites ───────────────────────────────────────────────

    override fun getAllFavourites(): Flow<List<FavouriteLocationEntity>> =
        favouriteLocationDao.getAllFavourites()

    override fun getFavouriteById(cityId: Long): Flow<FavouriteLocationEntity?> =
        favouriteLocationDao.getFavouriteById(cityId)

    override fun isFavourite(cityId: Long): Flow<Boolean> =
        favouriteLocationDao.isFavourite(cityId)

    override suspend fun insertFavourite(favourite: FavouriteLocationEntity) =
        favouriteLocationDao.insertFavourite(favourite)

    override suspend fun deleteFavouriteById(cityId: Long) =
        favouriteLocationDao.deleteFavouriteById(cityId)

    override suspend fun updateLastTemp(cityId: Long, temp: Double, iconUrl: String) =
        favouriteLocationDao.updateLastWeather(cityId, temp, iconUrl)

    // ── Alerts ───────────────────────────────────────────────

    override fun getAllAlerts(): Flow<List<AlertEntity>> =
        alertDao.getAllAlerts()

    override fun getScheduledAlerts(): Flow<List<AlertEntity>> =
        alertDao.getScheduledAlerts()

    override fun getWeatherAlerts(): Flow<List<AlertEntity>> =
        alertDao.getWeatherAlerts()

    override fun getAlertById(id: Long): Flow<AlertEntity?> = alertDao.getAlertById(id)
    
    override suspend fun insertAlert(alert: AlertEntity): Long =
        alertDao.insertAlert(alert)

    override suspend fun updateEnabled(id: Long, isEnabled: Boolean) =
        alertDao.updateEnabled(id, isEnabled)

    override suspend fun deleteAlert(id: Long) =
        alertDao.deleteAlert(id)

    override suspend fun deleteAllAlerts() =
        alertDao.deleteAllAlerts()

}