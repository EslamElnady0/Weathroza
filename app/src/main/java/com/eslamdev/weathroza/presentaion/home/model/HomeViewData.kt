package com.eslamdev.weathroza.presentaion.home.model

import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity


data class HomeViewData(
    val weather: WeatherEntity,
    val hourlyForecast: List<HourlyForecastEntity>,
    val dailyForecast: List<DailyForecastEntity>
)