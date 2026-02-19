package com.eslamdev.weathroza.data.config.network

import com.eslamdev.weathroza.data.datasources.remote.service.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    val weatherService: WeatherService =
        retrofit.create(WeatherService::class.java)
}