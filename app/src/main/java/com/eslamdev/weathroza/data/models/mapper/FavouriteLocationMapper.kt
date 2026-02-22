package com.eslamdev.weathroza.data.models.mapper

import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.google.android.gms.maps.model.LatLng

object FavouriteLocationMapper {
    fun toEntity(weatherEntity: WeatherEntity, latLng: LatLng): FavouriteLocationEntity {
        return FavouriteLocationEntity(
            cityId = weatherEntity.id.toLong(),
            name = weatherEntity.name,
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