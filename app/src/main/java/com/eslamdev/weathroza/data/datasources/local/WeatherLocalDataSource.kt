package com.eslamdev.weathroza.data.datasources.local

import android.content.Context
import com.eslamdev.weathroza.data.config.db.WeatherDataBase
import com.eslamdev.weathroza.data.datasources.local.dao.DailyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.FavouriteLocationDao
import com.eslamdev.weathroza.data.datasources.local.dao.HourlyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.WeatherDao
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(val context: Context) {
    private val weatherDao: WeatherDao = WeatherDataBase.getInstance(context).getWeatherDao()
    private val hourlyForecastDao: HourlyForecastDao =
        WeatherDataBase.getInstance(context).getHourlyForecastDao()
    private val dailyForecastDao: DailyForecastDao =
        WeatherDataBase.getInstance(context).getDailyForecastDao()
    private val favouriteLocationDao: FavouriteLocationDao =
        WeatherDataBase.getInstance(context).getFavouriteLocationDao()

    suspend fun insertWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

    suspend fun getWeatherByCity(cityName: String): WeatherEntity? {
        return weatherDao.getWeatherByCity(cityName)
    }

    suspend fun getAllWeather(): List<WeatherEntity> {
        return weatherDao.getAllWeatherList()
    }

    suspend fun deleteAllWeather() {
        weatherDao.deleteAllWeather()
    }

    suspend fun insertHourlyForecasts(forecasts: List<HourlyForecastEntity>) {
        hourlyForecastDao.insertHourlyForecasts(forecasts)
    }

    suspend fun getHourlyForecasts(): List<HourlyForecastEntity> {
        return hourlyForecastDao.getHourlyForecastsList()
    }

    suspend fun deleteAllHourlyForecasts() {
        hourlyForecastDao.deleteAllHourlyForecasts()
    }

    suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>) {
        dailyForecastDao.insertDailyForecasts(forecasts)
    }

    suspend fun deleteAllDailyForecasts() {
        dailyForecastDao.deleteAllDailyForecasts()
    }

    suspend fun deleteDailyForecastsByCityId(cityId: Long) {
        dailyForecastDao.deleteDailyForecastsByCity(cityId)
    }

    suspend fun deleteHourlyForecastsByCityId(cityId: Long) {
        hourlyForecastDao.deleteHourlyForecastsByCityId(cityId)
    }

    suspend fun deleteWeatherByCityId(cityId: Long) {
        weatherDao.deleteWeatherByCityId(cityId)
    }

    suspend fun getWeatherByCityId(cityId: Long): WeatherEntity? {
        return weatherDao.getWeatherByCityId(cityId)
    }

    suspend fun getHourlyForecastsByCityId(cityId: Long): List<HourlyForecastEntity> {
        return hourlyForecastDao.getHourlyForecastsByCityId(cityId)
    }

    suspend fun getDailyForecastsByCityId(cityId: Long): List<DailyForecastEntity> {
        return dailyForecastDao.getDailyForecastsList(cityId)
    }

    // ── Favourites ──────────────────────────────────────────────

    fun getAllFavourites(): Flow<List<FavouriteLocationEntity>> =
        favouriteLocationDao.getAllFavourites()

    fun getFavouriteById(cityId: Long): Flow<FavouriteLocationEntity?> =
        favouriteLocationDao.getFavouriteById(cityId)

    fun isFavourite(cityId: Long): Flow<Boolean> =
        favouriteLocationDao.isFavourite(cityId)

    suspend fun insertFavourite(favourite: FavouriteLocationEntity) {
        favouriteLocationDao.insertFavourite(favourite)
    }

    suspend fun deleteFavouriteById(cityId: Long) {
        favouriteLocationDao.deleteFavouriteById(cityId)
    }

    suspend fun updateLastTemp(cityId: Long, temp: Double, iconUrl: String) {
        favouriteLocationDao.updateLastWeather(cityId, temp, iconUrl)
    }
}

