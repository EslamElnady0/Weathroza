package com.eslamdev.weathroza.data.config.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eslamdev.weathroza.data.datasources.local.WeatherDao
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao

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
