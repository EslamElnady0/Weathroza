package com.eslamdev.weathroza.data.datasources.local

import com.eslamdev.weathroza.data.models.alert.AlertEntity

interface AlarmScheduler {
    fun scheduleAlert(alert: AlertEntity)
    fun cancelAlert(alertId: Long)
    fun scheduleContinuousAlerts()
}