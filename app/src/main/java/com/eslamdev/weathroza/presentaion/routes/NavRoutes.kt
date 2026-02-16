package com.eslamdev.weathroza.presentaion.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    object HomeRoute : Route()

}