package com.eslamdev.weathroza.data.models.alert

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val parameter: WeatherParameter,
    val threshold: Float,
    val isAbove: Boolean,
    val frequency: AlertFrequency,
    val thresholdTempUnit: TemperatureUnit? = null,
    val thresholdWindUnit: WindSpeedUnit? = null,
    val startTimeMillis: Long? = null,
    val endTimeMillis: Long? = null,
    val repeatDays: Set<AlertDay> = emptySet(),
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val notifyType: AlertNotifyType = AlertNotifyType.ALARM
)