package com.eslamdev.weathroza.presentaion.settings.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.settings.langmanager.LocaleHelper
import com.eslamdev.weathroza.core.settings.*
import com.eslamdev.weathroza.core.settings.location.LocationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SnackBarState(
    val isVisible: Boolean = false,
    val message: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class SettingsViewModel(
    private val dataStore: SettingsDataStore,
    private val context: Context
) : ViewModel() {

    val settings: StateFlow<UserSettings> = dataStore.settingsFlow
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
            dataStore.saveTemperatureUnit(TemperatureUnit.entries[index])
        }
    }
    fun onWindSpeedUnitChanged(index: Int) {
        viewModelScope.launch {
            dataStore.saveWindSpeedUnit(WindSpeedUnit.entries[index])
        }
    }
    fun onLanguageChanged(language: AppLanguage) {
        viewModelScope.launch {
            dataStore.saveLanguage(language)
        }
        LocaleHelper.setAppLanguage(language)
    }

    fun onGpsLocationSelected() {
        viewModelScope.launch {
            _snackBarState.value = SnackBarState(
                isVisible = true,
                message = context.getString(R.string.fetching_gps_location),
                isLoading = true
            )
            try {
                val location = LocationManager(context).getCurrentLocation()
                dataStore.saveGpsLocation(
                    lat    = location.latitude,
                    lng    = location.longitude,
                )
                _snackBarState.value = SnackBarState(
                    isVisible = true,
                    message = context.getString(R.string.gps_location_saved),
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("TAG", "GPS fetch failed: ${e.message}")
                _snackBarState.value = SnackBarState(
                    isVisible = true,
                    message = context.getString(R.string.gps_location_failed),
                    isError = true,
                    isLoading = false
                )
            }
        }
    }
}

class SettingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(SettingsDataStore(context),context) as T
    }
}