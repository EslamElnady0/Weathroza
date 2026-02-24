package com.eslamdev.weathroza

import android.content.Context
import com.eslamdev.weathroza.data.config.db.WeatherDataBase
import com.eslamdev.weathroza.data.config.network.RetrofitHelper
import com.eslamdev.weathroza.data.datasources.local.LocationManager
import com.eslamdev.weathroza.data.datasources.local.NetworkObserver
import com.eslamdev.weathroza.data.datasources.local.SettingsDataStore
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.local.dao.DailyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.FavouriteLocationDao
import com.eslamdev.weathroza.data.datasources.local.dao.HourlyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.WeatherDao
import com.eslamdev.weathroza.data.datasources.local.impl.LocationManagerImpl
import com.eslamdev.weathroza.data.datasources.local.impl.NetworkObserverImpl
import com.eslamdev.weathroza.data.datasources.local.impl.SettingsDataStoreImpl
import com.eslamdev.weathroza.data.datasources.local.impl.WeatherLocalDataSourceImpl
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource
import com.eslamdev.weathroza.data.datasources.remote.impl.WeatherRemoteDataSourceImpl
import com.eslamdev.weathroza.data.datasources.remote.service.WeatherService
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.data.repo.impl.UserSettingsRepoImpl
import com.eslamdev.weathroza.data.repo.impl.WeatherRepoImpl

interface AppContainer {
    val locationManager: LocationManager
    val settingsDataStore: SettingsDataStore
    val networkObserver: NetworkObserver
    val dailyForecastDao: DailyForecastDao
    val hourlyForecastDao: HourlyForecastDao
    val weatherDao: WeatherDao
    val favLocationDao: FavouriteLocationDao
    val weatherService: WeatherService
    val weatherLocalDataSource: WeatherLocalDataSource
    val weatherRemoteDataSource: WeatherRemoteDataSource
    val weatherRepo: WeatherRepo
    val userSettingsRepo: UserSettingsRepo
}

class AppContainerImpl(private val context: Context) : AppContainer {
    override val locationManager: LocationManager by lazy {
        LocationManagerImpl(context)
    }
    override val settingsDataStore: SettingsDataStore by lazy {
        SettingsDataStoreImpl(context)
    }
    override val networkObserver: NetworkObserver by lazy {
        NetworkObserverImpl(context)
    }

    override val dailyForecastDao: DailyForecastDao by lazy {
        WeatherDataBase.getInstance(context).getDailyForecastDao()
    }
    override val hourlyForecastDao: HourlyForecastDao by lazy {
        WeatherDataBase.getInstance(context).getHourlyForecastDao()
    }
    override val weatherDao: WeatherDao by lazy {
        WeatherDataBase.getInstance(context).getWeatherDao()
    }
    override val favLocationDao: FavouriteLocationDao by lazy {
        WeatherDataBase.getInstance(context).getFavouriteLocationDao()
    }
    override val weatherService: WeatherService by lazy {
        RetrofitHelper.weatherService
    }
    override val weatherLocalDataSource: WeatherLocalDataSource by lazy {
        WeatherLocalDataSourceImpl(
            weatherDao = weatherDao,
            hourlyForecastDao = hourlyForecastDao,
            dailyForecastDao = dailyForecastDao,
            favouriteLocationDao = favLocationDao
        )
    }
    override val weatherRemoteDataSource: WeatherRemoteDataSource by lazy {
        WeatherRemoteDataSourceImpl(weatherService)
    }
    override val weatherRepo: WeatherRepo by lazy {
        WeatherRepoImpl(
            localDataSource = weatherLocalDataSource,
            remoteDataSource = weatherRemoteDataSource,
        )
    }
    override val userSettingsRepo: UserSettingsRepo by lazy {
        UserSettingsRepoImpl(
            dataStore = settingsDataStore,
            locationManager = locationManager,
            networkObserver = networkObserver
        )
    }

}