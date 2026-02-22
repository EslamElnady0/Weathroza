package com.eslamdev.weathroza.presentaion.settings.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.settings.langmanager.LocaleHelper
import com.eslamdev.weathroza.core.settings.*
import com.eslamdev.weathroza.core.settings.location.LocationManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
            try {
                val location = LocationManager(context).getCurrentLocation()
                dataStore.saveGpsLocation(
                    lat    = location.latitude,
                    lng    = location.longitude,
                )
            } catch (e: Exception) {
                Log.e("TAG", "GPS fetch failed: ${e.message}")
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