package com.eslamdev.weathroza.data.datasources.local

import android.content.Context
import com.eslamdev.weathroza.data.config.db.WeatherDataBase

class WeatherLocalDataSource(val context: Context) {
    var weatherDao: WeatherDao = WeatherDataBase.getInstance(context).getWeatherDao()

}