package com.eslamdev.weathroza.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weather")
data class WeatherDto(@PrimaryKey val name: String)
