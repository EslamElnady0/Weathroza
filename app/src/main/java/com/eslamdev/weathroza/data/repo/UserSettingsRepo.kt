package com.eslamdev.weathroza.data.repo

import com.eslamdev.weathroza.core.network.NetworkObserver
import com.eslamdev.weathroza.data.datasources.local.LocationManager
import com.eslamdev.weathroza.data.datasources.local.SettingsDataStore
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.LocationType
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserSettingsRepo(
    private val dataStore: SettingsDataStore,
    private val locationManager: LocationManager,
    private val networkObserver: NetworkObserver
) {

    val settingsFlow: Flow<UserSettings> = dataStore.settingsFlow
    val isConnected: Flow<Boolean> = networkObserver.isConnected


    suspend fun getSettings(): UserSettings = dataStore.settingsFlow.first()


    suspend fun saveTemperatureUnit(unit: TemperatureUnit) =
        dataStore.saveTemperatureUnit(unit)

    suspend fun saveWindSpeedUnit(unit: WindSpeedUnit) =
        dataStore.saveWindSpeedUnit(unit)

    suspend fun saveLanguage(language: AppLanguage) =
        dataStore.saveLanguage(language)

    suspend fun saveLocationType(locationType: LocationType) =
        dataStore.saveLocationType(locationType)

    suspend fun saveManualLocation(lat: Double, lng: Double, cityId: Long) =
        dataStore.saveManualLocation(lat, lng, cityId)

    suspend fun saveCityId(cityId: Long) =
        dataStore.saveCityId(cityId)


    fun fetchAndSaveGpsLocation(): Flow<Result<Unit>> =
        locationManager.getLocationFlow()
            .map { location ->
                dataStore.saveGpsLocation(location.latitude, location.longitude)
                Result.success(Unit)
            }
            .catch { e -> emit(Result.failure(e)) }
}