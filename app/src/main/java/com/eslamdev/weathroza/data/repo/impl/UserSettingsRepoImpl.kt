package com.eslamdev.weathroza.data.repo.impl

import com.eslamdev.weathroza.data.datasources.local.LocationManager
import com.eslamdev.weathroza.data.datasources.local.NetworkObserver
import com.eslamdev.weathroza.data.datasources.local.SettingsDataStore
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.LocationType
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class UserSettingsRepoImpl(
    private val dataStore: SettingsDataStore,
    private val locationManager: LocationManager,
    private val networkObserver: NetworkObserver
) : UserSettingsRepo {

    override val settingsFlow: Flow<UserSettings> = dataStore.settingsFlow
    override val isConnected: Flow<Boolean> = networkObserver.isConnected

    override suspend fun getSettings(): UserSettings = dataStore.settingsFlow.first()

    override suspend fun saveTemperatureUnit(unit: TemperatureUnit) =
        dataStore.saveTemperatureUnit(unit)

    override suspend fun saveWindSpeedUnit(unit: WindSpeedUnit) =
        dataStore.saveWindSpeedUnit(unit)

    override suspend fun saveLanguage(language: AppLanguage) =
        dataStore.saveLanguage(language)

    override suspend fun saveLocationType(locationType: LocationType) =
        dataStore.saveLocationType(locationType)

    override suspend fun saveManualLocation(lat: Double, lng: Double, cityId: Long) =
        dataStore.saveManualLocation(lat, lng, cityId)

    override suspend fun saveCityId(cityId: Long) =
        dataStore.saveCityId(cityId)

    override fun fetchAndSaveGpsLocation(): Flow<Result<Unit>> =
        locationManager.getLocationFlow()
            .map { location ->
                dataStore.saveGpsLocation(location.latitude, location.longitude)
                Result.success(Unit)
            }
            .catch { e -> emit(Result.failure(e)) }
}