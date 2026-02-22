package com.eslamdev.weathroza.data.models.fav

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_locations")
data class FavouriteLocationEntity(
    @PrimaryKey
    val cityId: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lng: Double,
    val lastTemp: Double?,
    val icon: String,
    val iconUrl: String,
    val lastUpdated: Long = System.currentTimeMillis()
)