package com.eslamdev.weathroza.presentaion.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.presentaion.main.views.MainView
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModel
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModelFactory
import com.eslamdev.weathroza.presentaion.map.views.MapView

@Composable
fun App(modifier: Modifier = Modifier) {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = Route.MainRoute) {

        composable<Route.MainRoute> {
            MainView(controller)
        }

        composable<Route.MapRoute> {
            val context = LocalContext.current
            val factory = remember { MapViewModelFactory(WeatherRepo(context), context) }
            val viewModel: MapViewModel = viewModel(factory = factory)
            MapView(
                navController = controller,
                viewModel = viewModel
            )
        }
    }
}
