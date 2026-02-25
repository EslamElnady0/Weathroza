package com.eslamdev.weathroza.data.models.alert

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val parameter: WeatherParameter,
    val threshold: Float,
    val isAbove: Boolean,
    val frequency: AlertFrequency,
    val startTimeMillis: Long?,
    val endTimeMillis: Long?,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
)