package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastResponseDto

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
                weatherIcon = weather?.icon ?: "",
                formattedDayName = com.eslamdev.weathroza.core.helpers.DateTimeHelper.formatDayName(
                    dto.dt
                ),
                formattedDate = com.eslamdev.weathroza.core.helpers.DateTimeHelper.formatShortDate(
                    dto.dt
                ),
                formattedTemp = dto.temp.day.toString(),
                formattedFeelsLike = dto.feelsLike.day.toString(),
                iconResId = com.eslamdev.weathroza.R.drawable.dummy_sun_image,
                iconUrl = "https://openweathermap.org/img/wn/${weather?.icon}@4x.png"
            )
        }
    }
}
