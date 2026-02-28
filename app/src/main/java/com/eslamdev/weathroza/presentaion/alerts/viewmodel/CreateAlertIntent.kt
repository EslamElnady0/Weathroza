package com.eslamdev.weathroza.presentaion.alerts.viewmodel

import com.eslamdev.weathroza.data.models.alert.AlertDay
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.AlertNotifyType
import com.eslamdev.weathroza.data.models.alert.WeatherParameter

sealed class CreateAlertIntent {
    data class SetName(val name: String) : CreateAlertIntent()
    data class SetParameter(val param: WeatherParameter, val initialThreshold: Float) :
        CreateAlertIntent()

    data class SetThreshold(val value: Float) : CreateAlertIntent()
    data class SetAbove(val isAbove: Boolean) : CreateAlertIntent()
    data class SetFrequency(val frequency: AlertFrequency) : CreateAlertIntent()
    data class SetStartTime(val hour: Int, val minute: Int, val display: String) :
        CreateAlertIntent()

    data class SetEndTime(val hour: Int, val minute: Int, val display: String) : CreateAlertIntent()
    data class ToggleDay(val day: AlertDay) : CreateAlertIntent()
    data class SetNotifyType(val notifyType: AlertNotifyType) : CreateAlertIntent()
    object Submit : CreateAlertIntent()
}