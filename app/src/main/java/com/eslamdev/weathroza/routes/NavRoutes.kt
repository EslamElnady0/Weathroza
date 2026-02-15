package com.eslamdev.weathroza.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    object HomeScreen : Route()

}