package com.eslamdev.weathroza.data.models.mapper

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
            
            country = dto.sys.country,
            sunrise = dto.sys.sunrise,
            sunset = dto.sys.sunset,
            
            dt = dto.dt
        )
    }
   
}
