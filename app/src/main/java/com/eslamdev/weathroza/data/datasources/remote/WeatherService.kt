package com.eslamdev.weathroza.data.datasources.remote

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
}