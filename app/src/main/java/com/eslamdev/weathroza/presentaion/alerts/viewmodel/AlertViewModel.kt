package com.eslamdev.weathroza.presentaion.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.helpers.AlertTimeValidator
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.mapper.AlertMapper
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                settings = settings.value,
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

    private val _createAlertState = MutableStateFlow(CreateAlertUiState())
    val createAlertState: StateFlow<CreateAlertUiState> = _createAlertState.asStateFlow()

    fun onCreateAlertIntent(intent: CreateAlertIntent) {
        val current = _createAlertState.value
        _createAlertState.value = when (intent) {
            is CreateAlertIntent.SetName ->
                current.copy(alertName = intent.name)

            is CreateAlertIntent.SetParameter ->
                current.copy(
                    selectedParam = intent.param,
                    thresholdValue = intent.initialThreshold,
                    startError = null,
                    endError = null,
                )

            is CreateAlertIntent.SetAbove ->
                current.copy(isAbove = intent.isAbove)

            is CreateAlertIntent.SetFrequency -> if (intent.frequency == AlertFrequency.PERIODIC) {
                current.copy(
                    frequency = intent.frequency,
                    startHour = -1, startMinute = -1,
                    endHour = -1, endMinute = -1,
                    startTimeDisplay = null, endTimeDisplay = null,
                    startError = null, endError = null,
                )
            } else {
                current.copy(frequency = intent.frequency)
            }

            is CreateAlertIntent.SetStartTime -> {
                val startError = when {
                    !AlertTimeValidator.isInFuture(intent.hour, intent.minute) ->
                        R.string.error_start_time_past

                    !AlertTimeValidator.isWithinMaxFuture(intent.hour, intent.minute) ->
                        R.string.error_time_too_far

                    else -> null
                }
                val endError = if (current.endHour != -1) validateEnd(
                    intent.hour, intent.minute, current.endHour, current.endMinute
                ) else current.endError

                current.copy(
                    startHour = intent.hour,
                    startMinute = intent.minute,
                    startTimeDisplay = intent.display,
                    startError = startError,
                    endError = endError,
                )
            }

            is CreateAlertIntent.SetEndTime -> {
                val endError = validateEnd(
                    current.startHour, current.startMinute, intent.hour, intent.minute
                )
                current.copy(
                    endHour = intent.hour,
                    endMinute = intent.minute,
                    endTimeDisplay = intent.display,
                    endError = endError,
                )
            }

            is CreateAlertIntent.Submit -> current
            is CreateAlertIntent.SetThreshold ->
                current.copy(thresholdValue = intent.value)
        }

        if (intent is CreateAlertIntent.Submit && current.isFormValid) {
            viewModelScope.launch {
                val entity = AlertMapper.create(
                    name = current.alertName,
                    parameter = current.selectedParam,
                    threshold = current.thresholdValue,
                    isAbove = current.isAbove,
                    frequency = current.frequency,
                    startHour = current.startHour.takeIf { it != -1 },
                    startMinute = current.startMinute.takeIf { it != -1 },
                    endHour = current.endHour.takeIf { it != -1 },
                    endMinute = current.endMinute.takeIf { it != -1 },
                    settings = settings.value,
                )
                weatherRepo.insertAlert(entity)
                _createAlertState.value = CreateAlertUiState()
            }
        }
    }

    private fun validateEnd(
        startHour: Int, startMinute: Int,
        endHour: Int, endMinute: Int,
    ): Int? = when {
        !AlertTimeValidator.isWithinMaxFuture(endHour, endMinute) ->
            R.string.error_time_too_far

        startHour != -1 && !AlertTimeValidator.isEndAfterStart(
            startHour, startMinute, endHour, endMinute
        ) -> R.string.error_end_before_start

        else -> null
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