package com.eslamdev.weathroza.presentaion.routes

import com.eslamdev.weathroza.core.enums.MapMode
import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    object MainRoute : Route()

    @Serializable
    data class MapRoute(var mode: MapMode = MapMode.SELECT_LOCATION) : Route()

    @Serializable
    data class FavWeatherRoute(val lat: Double, val lng: Double, val cityId: Long) : Route()
}


@Serializable
sealed class BottomRoute {

    @Serializable
    object Home : BottomRoute()

    @Serializable
    object Favourites : BottomRoute()

    @Serializable
    object Alerts : BottomRoute()

    @Serializable
    object Settings : BottomRoute()
}