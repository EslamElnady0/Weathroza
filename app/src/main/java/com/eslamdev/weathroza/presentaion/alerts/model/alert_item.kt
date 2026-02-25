package com.eslamdev.weathroza.presentaion.alerts.model

data class AlertItem(
    val id: Int,
    val title: String,
    val timeRange: String,
    val location: String,
    val isEnabled: Boolean,
)