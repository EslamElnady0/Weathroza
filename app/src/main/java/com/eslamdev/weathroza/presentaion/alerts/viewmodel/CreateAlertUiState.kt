package com.eslamdev.weathroza.presentaion.alerts.viewmodel

import com.eslamdev.weathroza.data.models.alert.AlertDay
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter

data class CreateAlertUiState(
    val alertName: String = "",
    val selectedParam: WeatherParameter = WeatherParameter.TEMP,
    val isAbove: Boolean = true,
    val frequency: AlertFrequency = AlertFrequency.TIME_BASED,
    val selectedDays: Set<AlertDay> = emptySet(),
    val startHour: Int = -1,
    val startMinute: Int = -1,
    val endHour: Int = -1,
    val endMinute: Int = -1,
    val thresholdValue: Float = 0f,
    val startTimeDisplay: String? = null,
    val endTimeDisplay: String? = null,
    val startError: Int? = null,
    val endError: Int? = null,
) {
    val isFormValid: Boolean
        get() = alertName.isNotBlank()
                && startError == null
                && endError == null
                && when (frequency) {
            AlertFrequency.TIME_BASED -> startHour != -1 && selectedDays.isNotEmpty()
            AlertFrequency.CONTINUOUS -> true
        }
}