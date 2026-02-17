package com.eslamdev.weathroza.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyForecastDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>)
    
    @Query("SELECT * FROM daily_forecast WHERE cityId = :cityId ORDER BY dt ASC")
    fun getDailyForecastsFlow(cityId: Long): Flow<List<DailyForecastEntity>>
    
    @Query("SELECT * FROM daily_forecast WHERE cityId = :cityId ORDER BY dt ASC")
    suspend fun getDailyForecastsList(cityId: Long): List<DailyForecastEntity>
    
    @Query("DELETE FROM daily_forecast WHERE cityId = :cityId")
    suspend fun deleteDailyForecastsByCity(cityId: Long)
    
    @Query("DELETE FROM daily_forecast")
    suspend fun deleteAllDailyForecasts()
}
