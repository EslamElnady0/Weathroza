package com.eslamdev.weathroza.data.repo

import android.content.Context
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource

class WeatherRepo(val context: Context) {
    val localDataSource = WeatherLocalDataSource(context)
    val remoteDataSource = WeatherRemoteDataSource()

}