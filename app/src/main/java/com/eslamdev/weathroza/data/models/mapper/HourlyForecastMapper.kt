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
                iconResId = com.eslamdev.weathroza.R.drawable.dummy_sun_image,
                cityId = response.city.id,
                iconUrl = "https://openweathermap.org/img/wn/${dto.weather.firstOrNull()?.icon}@4x.png"
            )
        }
    }
}
