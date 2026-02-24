package com.eslamdev.weathroza.data.repo

import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.LocationType
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepo {

    val settingsFlow: Flow<UserSettings>
    val isConnected: Flow<Boolean>

    suspend fun getSettings(): UserSettings

    suspend fun saveTemperatureUnit(unit: TemperatureUnit)
    suspend fun saveWindSpeedUnit(unit: WindSpeedUnit)
    suspend fun saveLanguage(language: AppLanguage)
    suspend fun saveLocationType(locationType: LocationType)
    suspend fun saveManualLocation(lat: Double, lng: Double, cityId: Long)
    suspend fun saveCityId(cityId: Long)

    fun fetchAndSaveGpsLocation(): Flow<Result<Unit>>
}