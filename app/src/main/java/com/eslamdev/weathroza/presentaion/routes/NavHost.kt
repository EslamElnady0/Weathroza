package com.eslamdev.weathroza.presentaion.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.presentaion.main.views.MainView
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModel
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModelFactory
import com.eslamdev.weathroza.presentaion.map.views.MapView
import com.eslamdev.weathroza.presentaion.favourite.view.FavWeatherDisplayView
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavWeatherDisplayViewModel
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavWeatherDisplayViewModelFactory

@Composable
fun App(modifier: Modifier = Modifier) {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = Route.MainRoute) {

        composable<Route.MainRoute> {
            MainView(controller)
        }

        composable<Route.MapRoute> { backStackEntry ->
            val context = LocalContext.current
            val args = backStackEntry.toRoute<Route.MapRoute>()
            val factory = remember {
                MapViewModelFactory(WeatherRepo(context), context, args.mode)
            }
            val viewModel: MapViewModel = viewModel(factory = factory)
            MapView(
                navController = controller,
                viewModel = viewModel
            )
        }

        composable<Route.FavWeatherRoute> { backStackEntry ->
            val context = LocalContext.current
            val args = backStackEntry.toRoute<Route.FavWeatherRoute>()
            val factory = remember {
                FavWeatherDisplayViewModelFactory(WeatherRepo(context), context, args.lat, args.lng, args.cityId)
            }
            val viewModel: FavWeatherDisplayViewModel = viewModel(factory = factory)
            FavWeatherDisplayView(
                viewModel = viewModel,
                bottomController = controller,
                onNavigateBack = { controller.popBackStack() }
            )
        }
    }
}
