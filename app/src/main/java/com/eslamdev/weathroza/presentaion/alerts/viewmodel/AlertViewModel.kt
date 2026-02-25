package com.eslamdev.weathroza.presentaion.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.presentaion.alerts.model.AlertFrequency
import com.eslamdev.weathroza.presentaion.alerts.model.AlertItem
import com.eslamdev.weathroza.presentaion.alerts.model.WeatherParameter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AlertsViewModel(
    private val settingsRepo: UserSettingsRepo,
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepo.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings(),
        )

    val scheduledAlerts = listOf(
        AlertItem(1, "Morning Briefing", "08:00 AM - 09:30 AM", "New York, NY", true),
        AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
    )
    
    fun toggleAlert(id: Int, enabled: Boolean) {
    }

    fun createAlert(
        name: String,
        parameter: WeatherParameter,
        threshold: Float,
        isAbove: Boolean,
        frequency: AlertFrequency,
        startTime: String?,
        endTime: String?,
    ) {
    }
}

class AlertViewModelFactory(private val settingsRepo: UserSettingsRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AlertsViewModel(settingsRepo) as T
    }
}