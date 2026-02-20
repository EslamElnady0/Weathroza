package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.geocoding.CityDto
import com.eslamdev.weathroza.data.models.geocoding.CityEntity

object CityMapper {
    fun toEntity(dto: CityDto): CityEntity {
        val fallbackName = dto.name ?: ""
        return CityEntity(
            nameEn = dto.localNames?.en ?: fallbackName,
            nameAr = dto.localNames?.ar ?: fallbackName,
            lat = dto.lat,
            lon = dto.lon,
            country = dto.country ?: "",
            state = dto.state ?: ""
        )
    }

    fun toEntityList(dtos: List<CityDto>): List<CityEntity> {
        return dtos.map { toEntity(it) }
    }
}
