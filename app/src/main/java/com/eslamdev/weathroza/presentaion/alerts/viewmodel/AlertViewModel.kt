package com.eslamdev.weathroza.presentaion.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.mapper.AlertMapper
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertsViewModel(
    private val settingsRepo: UserSettingsRepo,
    private val weatherRepo: WeatherRepo,
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepo.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    val alerts: StateFlow<UiState<List<AlertEntity>>> = weatherRepo.getAllAlerts()
        .map { result ->
            result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(message = it.message ?: "Unknown error") },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading,
        )


    fun createAlert(
        name: String,
        parameter: WeatherParameter,
        threshold: Float,
        isAbove: Boolean,
        frequency: AlertFrequency,
        startHour: Int?,
        startMinute: Int?,
        endHour: Int?,
        endMinute: Int?,
    ) {
        viewModelScope.launch {
            val entity = AlertMapper.create(
                name = name,
                parameter = parameter,
                threshold = threshold,
                isAbove = isAbove,
                frequency = frequency,
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
            )
            weatherRepo.insertAlert(entity)
        }
    }

    fun toggleAlert(id: Long, isEnabled: Boolean) {
        viewModelScope.launch { weatherRepo.toggleAlert(id, isEnabled) }
    }

    fun deleteAlert(id: Long) {
        viewModelScope.launch { weatherRepo.deleteAlert(id) }
    }
}

class AlertViewModelFactory(
    private val settingsRepo: UserSettingsRepo,
    private val weatherRepo: WeatherRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AlertsViewModel(settingsRepo, weatherRepo) as T
    }
}