package com.eslamdev.weathroza.data.datasources.local

import android.content.Context
import com.eslamdev.weathroza.data.config.db.WeatherDataBase
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

class WeatherLocalDataSource(val context: Context) {
    private val weatherDao: WeatherDao = WeatherDataBase.getInstance(context).getWeatherDao()
    private val hourlyForecastDao: HourlyForecastDao =
        WeatherDataBase.getInstance(context).getHourlyForecastDao()

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
}