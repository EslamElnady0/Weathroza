package com.example.workmanagerproducts.data.repo

import android.content.Context
import com.example.workmanagerproducts.data.datasources.local.WeatherLocalDataSource
import com.example.workmanagerproducts.data.datasources.remote.WeatherRemoteDataSource
import com.example.workmanagerproducts.data.models.Product

class WeatherRepo(val context: Context) {
    val localDataSource = WeatherLocalDataSource(context)
    val remoteDataSource = WeatherRemoteDataSource()

    suspend fun getProducts() = remoteDataSource.getProducts()

    fun getFavProducts() = localDataSource.getFavProducts()

    suspend fun insertProductInFav(product: Product) = localDataSource.insertProduct(product)

    suspend fun removeProductFromFav(product: Product) = localDataSource.removeProduct(product)
}