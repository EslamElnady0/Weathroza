package com.eslamdev.weathroza.data.models.geocoding

data class CityEntity(
    val nameEn: String,
    val nameAr: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String
)
