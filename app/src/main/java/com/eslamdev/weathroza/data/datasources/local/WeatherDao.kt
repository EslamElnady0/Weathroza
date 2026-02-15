package com.example.workmanagerproducts.data.datasources.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workmanagerproducts.data.models.Product

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProduct(prod: Product)

    @Query("SELECT * FROM product")
    fun getAll(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(prodList: List<Product>)

    @Delete
    suspend fun removeProduct(prod: Product)
}