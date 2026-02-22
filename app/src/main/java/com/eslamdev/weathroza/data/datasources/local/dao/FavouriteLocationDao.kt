package com.eslamdev.weathroza.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteLocationDao {

    @Query("SELECT * FROM favourite_locations ORDER BY name ASC")
    fun getAllFavourites(): Flow<List<FavouriteLocationEntity>>

    @Query("SELECT * FROM favourite_locations WHERE cityId = :cityId")
    fun getFavouriteById(cityId: Long): Flow<FavouriteLocationEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_locations WHERE cityId = :cityId)")
    fun isFavourite(cityId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: FavouriteLocationEntity)

    @Delete
    suspend fun deleteFavourite(favourite: FavouriteLocationEntity)

    @Query("DELETE FROM favourite_locations WHERE cityId = :cityId")
    suspend fun deleteFavouriteById(cityId: Long)

    @Query("""
    UPDATE favourite_locations 
    SET lastTemp = :temp, iconUrl = :iconUrl, lastUpdated = :timestamp 
    WHERE cityId = :cityId
""")
    suspend fun updateLastWeather(
        cityId: Long,
        temp: Double,
        iconUrl: String,
        timestamp: Long = System.currentTimeMillis()
    )
}