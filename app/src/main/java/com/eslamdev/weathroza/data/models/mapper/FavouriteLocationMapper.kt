package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.google.android.gms.maps.model.LatLng

object FavouriteLocationMapper {
    fun toEntity(
        weatherEntity: WeatherEntity,
        latLng: LatLng,
        cityEntity: CityEntity
    ): FavouriteLocationEntity {
        return FavouriteLocationEntity(
            locationName = weatherEntity.name,
            cityId = weatherEntity.id.toLong(),
            enName = cityEntity.nameEn,
            arName = cityEntity.nameAr,
            country = weatherEntity.country.orEmpty(),
            lat = latLng.latitude,
            lng = latLng.longitude,
            lastTemp = weatherEntity.temp,
            icon = weatherEntity.weatherIcon,
            iconUrl = weatherEntity.iconUrl,
            lastUpdated = System.currentTimeMillis()
        )
    }
}