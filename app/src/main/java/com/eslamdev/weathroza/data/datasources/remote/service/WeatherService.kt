package com.eslamdev.weathroza.data.datasources.remote

import com.eslamdev.weathroza.data.models.forecast.DailyForecastResponseDto
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastResponseDto
import com.eslamdev.weathroza.data.models.weather.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeather(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherDto

    @GET("forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int = 24
    ): HourlyForecastResponseDto

    @GET("forecast/daily")
    suspend fun getDailyForecast(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") count: Int = 7,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric"
    ): DailyForecastResponseDto
}