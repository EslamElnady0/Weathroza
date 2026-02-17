package com.eslamdev.weathroza.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HourlyForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecasts(forecasts: List<HourlyForecastEntity>)

    @Query("SELECT * FROM hourly_forecast ORDER BY dt ASC")
    fun getHourlyForecasts(): Flow<List<HourlyForecastEntity>>

    @Query("SELECT * FROM hourly_forecast ORDER BY dt ASC")
    suspend fun getHourlyForecastsList(): List<HourlyForecastEntity>

    @Query("DELETE FROM hourly_forecast")
    suspend fun deleteAllHourlyForecasts()
}
