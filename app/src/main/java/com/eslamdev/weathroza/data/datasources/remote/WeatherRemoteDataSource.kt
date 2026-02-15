package com.eslamdev.weathroza.data.datasources.remote

import com.eslamdev.weathroza.data.config.network.RetrofitHelper

class WeatherRemoteDataSource {
    val weatherService: WeatherService = RetrofitHelper.productService
}