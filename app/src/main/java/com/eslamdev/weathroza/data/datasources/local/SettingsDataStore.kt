package com.eslamdev.weathroza.data.datasources.local

import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.LocationType
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface SettingsDataStore {
    val settingsFlow: Flow<UserSettings>
    suspend fun saveTemperatureUnit(unit: TemperatureUnit)
    suspend fun saveWindSpeedUnit(unit: WindSpeedUnit)
    suspend fun saveLanguage(language: AppLanguage)
    suspend fun saveLocationType(type: LocationType)
    suspend fun saveManualLocation(lat: Double, lng: Double, cityId: Long)
    suspend fun saveGpsLocation(lat: Double, lng: Double)
    suspend fun saveCityId(cityId: Long)
}