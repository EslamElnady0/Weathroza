package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.forecast.HourlyForecastDto
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastResponseDto

object HourlyForecastMapper {
    
    fun toEntityList(response: HourlyForecastResponseDto): List<HourlyForecastEntity> {
        return response.list.map { dto ->
            HourlyForecastEntity(
                dt = dto.dt,
                temp = dto.main.temp,
                icon = dto.weather.firstOrNull()?.icon ?: ""
            )
        }
    }
}
