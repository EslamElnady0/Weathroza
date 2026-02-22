package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.weather.WeatherDto

object FavouriteLocationMapper {

    fun toEntity(dto: WeatherDto): FavouriteLocationEntity {
        val weather = dto.weather.firstOrNull()
        val iconCode = weather?.icon.orEmpty()

        return FavouriteLocationEntity(
            cityId      = dto.id.toLong(),
            name        = dto.name,
            country     = dto.sys?.country.orEmpty(),
            lat         = dto.coord.lat,
            lng         = dto.coord.lon,
            lastTemp    = dto.main.temp,
            icon        = iconCode,
            iconUrl     = "https://openweathermap.org/img/wn/${iconCode}@4x.png",
            lastUpdated = System.currentTimeMillis()
        )
    }

    fun toEntityList(dtos: List<WeatherDto>): List<FavouriteLocationEntity> =
        dtos.map { toEntity(it) }
}