package com.eslamdev.weathroza.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity): Long

    @Query("SELECT * FROM alerts WHERE id = :id LIMIT 1")
    fun getAlertById(id: Long): Flow<AlertEntity?>

    @Query("SELECT * FROM alerts ORDER BY createdAt DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE frequency = 'ONE_TIME' ORDER BY createdAt DESC")
    fun getScheduledAlerts(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE frequency = 'PERIODIC' ORDER BY createdAt DESC")
    fun getWeatherAlerts(): Flow<List<AlertEntity>>

    @Query("UPDATE alerts SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun updateEnabled(id: Long, isEnabled: Boolean)

    @Query("DELETE FROM alerts WHERE id = :id")
    suspend fun deleteAlert(id: Long)

    @Query("DELETE FROM alerts")
    suspend fun deleteAllAlerts()

    @Query("UPDATE alerts SET startTimeMillis = :newStartMillis WHERE id = :alertId")
    suspend fun updateAlertStartTime(alertId: Long, newStartMillis: Long)
}