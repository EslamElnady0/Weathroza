package com.eslamdev.weathroza

import android.app.AlarmManager
import android.content.Context
import com.eslamdev.weathroza.data.config.db.WeatherDataBase
import com.eslamdev.weathroza.data.config.network.RetrofitHelper
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.datasources.local.LocationManager
import com.eslamdev.weathroza.data.datasources.local.NetworkObserver
import com.eslamdev.weathroza.data.datasources.local.SettingsDataStore
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.local.dao.AlertDao
import com.eslamdev.weathroza.data.datasources.local.dao.DailyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.FavouriteLocationDao
import com.eslamdev.weathroza.data.datasources.local.dao.HourlyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.WeatherDao
import com.eslamdev.weathroza.data.datasources.local.impl.AlarmSchedulerImpl
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
    val alertDao: AlertDao
    val weatherService: WeatherService
    val weatherLocalDataSource: WeatherLocalDataSource
    val weatherRemoteDataSource: WeatherRemoteDataSource
    val alarmScheduler: AlarmScheduler
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
    override val alertDao: AlertDao by lazy {
        WeatherDataBase.getInstance(context).getAlertDao()
    }
    override val weatherService: WeatherService by lazy {
        RetrofitHelper.weatherService
    }
    override val weatherLocalDataSource: WeatherLocalDataSource by lazy {
        WeatherLocalDataSourceImpl(
            weatherDao = weatherDao,
            hourlyForecastDao = hourlyForecastDao,
            dailyForecastDao = dailyForecastDao,
            favouriteLocationDao = favLocationDao,
            alertDao = alertDao
        )
    }
    override val weatherRemoteDataSource: WeatherRemoteDataSource by lazy {
        WeatherRemoteDataSourceImpl(weatherService)
    }
    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override val alarmScheduler: AlarmScheduler by lazy {
        AlarmSchedulerImpl(context, alarmManager)
    }

    override val weatherRepo: WeatherRepo by lazy {
        WeatherRepoImpl(
            localDataSource = weatherLocalDataSource,
            remoteDataSource = weatherRemoteDataSource,
            alarmScheduler = alarmScheduler
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