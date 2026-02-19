package com.eslamdev.weathroza.data.models.forecast

import com.google.gson.annotations.SerializedName

data class DailyForecastResponseDto(
    @SerializedName("city")
    val city: CityDto,
    @SerializedName("list")
    val list: List<DailyForecastItemDto>
)

data class CityDto(
    @SerializedName("id")
    val id: Long
)

data class DailyForecastItemDto(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("temp")
    val temp: TempDto,
    @SerializedName("feels_like")
    val feelsLike: FeelsLikeDto,
    @SerializedName("weather")
    val weather: List<DailyWeatherDto>
)

data class TempDto(
    @SerializedName("day")
    val day: Double
)

data class FeelsLikeDto(
    @SerializedName("day")
    val day: Double
)

data class DailyWeatherDto(
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)
