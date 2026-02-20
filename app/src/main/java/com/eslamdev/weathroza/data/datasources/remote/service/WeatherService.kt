package com.eslamdev.weathroza.data.datasources.remote.service

import com.eslamdev.weathroza.data.models.forecast.DailyForecastResponseDto
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastResponseDto
import com.eslamdev.weathroza.data.models.geocoding.CityDto
import com.eslamdev.weathroza.data.models.weather.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query
private const val dataPrefix = "data/2.5"
interface WeatherService {
    @GET("$dataPrefix/weather")
    suspend fun getWeather(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherDto

    @GET("${dataPrefix}/forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int = 24
    ): HourlyForecastResponseDto

    @GET("${dataPrefix}/forecast/daily")
    suspend fun getDailyForecast(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") count: Int = 7,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "metric"
    ): DailyForecastResponseDto

    @GET("geo/1.0/direct")
    suspend fun getPossibleCities(
        @Query("appid") apiKey: String,
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 5
    ): List<CityDto>
}