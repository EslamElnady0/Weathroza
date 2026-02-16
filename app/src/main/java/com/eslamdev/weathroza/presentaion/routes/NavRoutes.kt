package com.eslamdev.weathroza.presentaion.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    object MainRoute : Route()

    @Serializable
    object MapRoute : Route()

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