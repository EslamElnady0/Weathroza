package com.eslamdev.weathroza.data.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.workmanagerproducts.data.datasources.local.WeatherDao

@Database(version = 1)
abstract class ProductsDataBase : RoomDatabase() {
    abstract fun getProductsDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: ProductsDataBase? = null
        fun getInstance(ctx: Context): ProductsDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, ProductsDataBase::class.java, "color_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
