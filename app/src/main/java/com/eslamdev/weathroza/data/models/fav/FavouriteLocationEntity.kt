package com.eslamdev.weathroza.data.models.fav

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_locations")
data class FavouriteLocationEntity(
    @PrimaryKey
    val cityId: Long,
    val locationName: String = "",
    val arName: String = "",
    val enName: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val lastTemp: Double? = null,
    val icon: String = "",
    val iconUrl: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)