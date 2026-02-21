package com.eslamdev.weathroza.presentaion.settings.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.settings.langmanager.LocaleHelper
import com.eslamdev.weathroza.core.settings.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: SettingsDataStore
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
            dataStore.saveLocationType(LocationType.GPS)
        }
    }
}

class SettingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(SettingsDataStore(context)) as T
    }
}