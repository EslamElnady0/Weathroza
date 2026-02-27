package com.eslamdev.weathroza.data.config.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eslamdev.weathroza.data.datasources.local.dao.AlertDao
import com.eslamdev.weathroza.data.datasources.local.dao.DailyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.FavouriteLocationDao
import com.eslamdev.weathroza.data.datasources.local.dao.HourlyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.WeatherDao
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

@TypeConverters(AlertConverters::class)
@Database(
    entities = [WeatherEntity::class, HourlyForecastEntity::class, DailyForecastEntity::class, FavouriteLocationEntity::class, AlertEntity::class],
    version = 4
)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
    abstract fun getHourlyForecastDao(): HourlyForecastDao
    abstract fun getDailyForecastDao(): DailyForecastDao
    abstract fun getFavouriteLocationDao(): FavouriteLocationDao

    abstract fun getAlertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance(ctx: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBase::class.java, "color_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
