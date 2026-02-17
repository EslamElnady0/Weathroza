package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.forecast.DailyForecastResponseDto
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity

object DailyForecastMapper {
    fun toEntityList(response: DailyForecastResponseDto): List<DailyForecastEntity> {
        val cityId = response.city.id
        return response.list.map { dto ->
            val weather = dto.weather.firstOrNull()
            DailyForecastEntity(
                cityId = cityId,
                dt = dto.dt,
                tempDay = dto.temp.day,
                feelsLikeDay = dto.feelsLike.day,
                weatherMain = weather?.main ?: "",
                weatherDescription = weather?.description ?: "",
                weatherIcon = weather?.icon ?: ""
            )
        }
    }
}
