package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastResponseDto

object HourlyForecastMapper {

    fun toEntityList(response: HourlyForecastResponseDto): List<HourlyForecastEntity> {
        return response.list.map { dto ->
            HourlyForecastEntity(
                dt = dto.dt,
                temp = dto.main.temp,
                icon = dto.weather.firstOrNull()?.icon ?: "",
                formattedTime = com.eslamdev.weathroza.core.helpers.DateTimeHelper.formatHour(dto.dt),
                formattedTemp = dto.main.temp.toInt().toString(),
                iconResId = com.eslamdev.weathroza.R.drawable.dummy_sun_image,
                iconUrl = "https://openweathermap.org/img/wn/${dto.weather.firstOrNull()?.icon}@4x.png"
            )
        }
    }
}
