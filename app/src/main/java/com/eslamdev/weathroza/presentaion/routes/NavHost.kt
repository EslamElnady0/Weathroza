package com.eslamdev.weathroza.presentaion.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eslamdev.weathroza.presentaion.home.view.HomeView

@Composable
fun App(modifier: Modifier = Modifier) {
    val controller = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = controller, startDestination = Route.HomeRoute) {
        composable<Route.HomeRoute> {
            HomeView(controller)
        }
    }
}

