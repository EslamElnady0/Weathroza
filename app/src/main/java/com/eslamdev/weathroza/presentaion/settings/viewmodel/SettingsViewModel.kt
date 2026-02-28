package com.eslamdev.weathroza.presentaion.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.settings.langmanager.LocaleHelper
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SnackBarState(
    val isVisible: Boolean = false,
    val messageRes: Int? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class SettingsViewModel(
    private val settingsRepo: UserSettingsRepo,
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepo.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserSettings()
        )

    private val _snackBarState = MutableStateFlow(SnackBarState())
    val snackBarState = _snackBarState.asStateFlow()

    fun onSnackBarDismissed() {
        _snackBarState.value = _snackBarState.value.copy(isVisible = false)
    }

    fun onTemperatureUnitChanged(index: Int) {
        viewModelScope.launch {
            settingsRepo.saveTemperatureUnit(TemperatureUnit.entries[index])
        }
    }

    fun onWindSpeedUnitChanged(index: Int) {
        viewModelScope.launch {
            settingsRepo.saveWindSpeedUnit(WindSpeedUnit.entries[index])
        }
    }

    fun onLanguageChanged(language: AppLanguage) {
        viewModelScope.launch {
            settingsRepo.saveLanguage(language)
        }
        LocaleHelper.setAppLanguage(language)
    }

    fun onGpsLocationSelected() {
        _snackBarState.value = SnackBarState(
            isVisible = true,
            messageRes = R.string.fetching_gps_location,
            isLoading = true
        )

        settingsRepo.fetchAndSaveGpsLocation()
            .onEach { result ->
                result.fold(
                    onSuccess = {
                        _snackBarState.value = SnackBarState(
                            isVisible = true,
                            messageRes = R.string.gps_location_saved,
                        )
                    },
                    onFailure = { e ->
                        Log.e("SettingsVM", "GPS fetch failed: ${e.message}")
                        _snackBarState.value = SnackBarState(
                            isVisible = true,
                            messageRes = R.string.gps_location_failed,
                            isError = true,
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}
