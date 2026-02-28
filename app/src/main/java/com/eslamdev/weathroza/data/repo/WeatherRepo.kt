package com.eslamdev.weathroza.data.repo

import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {

    fun getWeatherFromApi(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units = Units.METRIC
    ): Flow<Result<WeatherEntity>>

    fun refreshHomeData(
        latitude: Double,
        longitude: Double,
        language: AppLanguage,
        units: Units = Units.METRIC
    ): Flow<Result<Triple<WeatherEntity, List<HourlyForecastEntity>, List<DailyForecastEntity>>>>

    suspend fun getCachedHomeData(
        cityId: Long?
    ): Triple<WeatherEntity, List<HourlyForecastEntity>, List<DailyForecastEntity>>?

    suspend fun clearCityData(cityId: Long)

    // ── Cities ──────────────────────────────────────────────────

    fun getPossibleCities(cityName: String, limit: Int): Flow<Result<List<CityEntity>>>

    fun getCityNamesLocalized(
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Flow<Result<List<CityEntity>>>

    // ── Favourites ──────────────────────────────────────────────

    fun getAllFavourites(): Flow<Result<List<FavouriteLocationEntity>>>

    fun isFavourite(cityId: Long): Flow<Result<Boolean>>

    suspend fun addFavourite(weatherEntity: WeatherEntity, latLng: LatLng, cityEntity: CityEntity)

    suspend fun removeFavourite(cityId: Long)

    suspend fun refreshFavouriteWeather(cityId: Long, temp: Double, iconUrl: String)

    // ── Alerts ──────────────────────────────────────────────
    fun getAllAlerts(): Flow<Result<List<AlertEntity>>>
    fun getAlertById(id: Long): Flow<Result<AlertEntity?>>
    suspend fun insertAlert(alert: AlertEntity): Long
    suspend fun toggleAlert(id: Long, isEnabled: Boolean)
    suspend fun deleteAlert(id: Long)
    suspend fun cancelTimeBasedAlert(alertId: Long)
    suspend fun updateAlertStartTime(alertId: Long, newStartMillis: Long)
    fun getContinuousAlerts(): Flow<Result<List<AlertEntity>>>
    fun startContinuousAlertsIfNeeded()
}
