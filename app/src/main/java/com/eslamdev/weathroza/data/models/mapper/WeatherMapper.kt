package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.data.models.weather.WeatherDto
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

object WeatherMapper {

    fun toEntity(dto: WeatherDto): WeatherEntity {
        val weather = dto.weather.firstOrNull()

        return WeatherEntity(
            id = dto.id,
            name = dto.name,

            weatherMain = weather?.main ?: "",
            weatherDescription = weather?.description ?: "",
            weatherIcon = weather?.icon ?: "",

            temp = dto.main.temp,
            feelsLike = dto.main.feelsLike,
            tempMin = dto.main.tempMin,
            tempMax = dto.main.tempMax,
            pressure = dto.main.pressure,
            humidity = dto.main.humidity,
            seaLevel = dto.main.seaLevel,
            grndLevel = dto.main.grndLevel,

            windSpeed = dto.wind.speed,
            windDeg = dto.wind.deg,
            windGust = dto.wind.gust,

            cloudsAll = dto.clouds.all,

            country = dto.sys?.country.orEmpty(),
            sunrise = dto.sys?.sunrise ?: 0L,
            sunset  = dto.sys?.sunset  ?: 0L,

            dt = dto.dt,
            formattedFullDate = DateTimeHelper.formatFullDateTime(
                dto.dt
            ),
            formattedSunrise = DateTimeHelper.formatTime(dto.sys?.sunrise ?: 0L),
            formattedSunset  = DateTimeHelper.formatTime(dto.sys?.sunset  ?: 0L),
            formattedTemp = dto.main.temp.toString(),
            iconUrl = "https://openweathermap.org/img/wn/${weather?.icon}@4x.png"
        )
    }

}
