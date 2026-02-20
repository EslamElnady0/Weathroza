package com.eslamdev.weathroza.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        val TEMP_UNIT = stringPreferencesKey("temp_unit")
        val WIND_UNIT = stringPreferencesKey("wind_unit")
        val LANGUAGE = stringPreferencesKey("language")
    }

    val settingsFlow: Flow<UserSettings> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            UserSettings(
                temperatureUnit = TemperatureUnit.valueOf(
                    prefs[TEMP_UNIT] ?: TemperatureUnit.CELSIUS.name
                ),
                windSpeedUnit = WindSpeedUnit.valueOf(
                    prefs[WIND_UNIT] ?: WindSpeedUnit.MS.name
                ),
                language = AppLanguage.valueOf(
                    prefs[LANGUAGE] ?: AppLanguage.SYSTEM.name
                )
            )
        }

    suspend fun saveTemperatureUnit(unit: TemperatureUnit) {
        context.dataStore.edit { it[TEMP_UNIT] = unit.name }
    }

    suspend fun saveWindSpeedUnit(unit: WindSpeedUnit) {
        context.dataStore.edit { it[WIND_UNIT] = unit.name }
    }

    suspend fun saveLanguage(language: AppLanguage) {
        context.dataStore.edit { it[LANGUAGE] = language.name }
    }
}