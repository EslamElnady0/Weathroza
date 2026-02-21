package com.eslamdev.weathroza.data.models.forecast

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponseDto(
    @SerializedName("list")
    val list: List<HourlyForecastDto>,
    @SerializedName("city")
    val city: City
)

data class HourlyForecastDto(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("main")
    val main: Main,
    @SerializedName("weather")
    val weather: List<Weather>
)

data class Main(
    @SerializedName("temp")
    val temp: Double
)

data class Weather(
    @SerializedName("icon")
    val icon: String
)

data class City(
    @SerializedName("id")
    val id: String
)