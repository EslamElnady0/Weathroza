package com.eslamdev.weathroza.data.datasources.local

import android.content.Context
import com.eslamdev.weathroza.data.core.db.ProductsDataBase
import com.example.workmanagerproducts.data.datasources.local.WeatherDao

class WeatherLocalDataSource(val context: Context) {
    var weatherDao: WeatherDao = ProductsDataBase.getInstance(context).getProductsDao()

}