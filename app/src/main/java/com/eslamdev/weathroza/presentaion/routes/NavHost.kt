package com.eslamdev.weathroza.presentaion.routes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.eslamdev.weathroza.AppViewModel
import com.eslamdev.weathroza.AppViewModelFactory
import com.eslamdev.weathroza.WeathrozaApp
import com.eslamdev.weathroza.presentaion.favourite.view.FavWeatherDisplayView
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavWeatherDisplayViewModel
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavWeatherDisplayViewModelFactory
import com.eslamdev.weathroza.presentaion.main.views.MainView
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModel
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModelFactory
import com.eslamdev.weathroza.presentaion.map.views.MapView

@Composable
fun App(modifier: Modifier = Modifier) {
    val controller = rememberNavController()
    val context = LocalContext.current
    val appContainer = remember {
        (context.applicationContext as WeathrozaApp).appContainer
    }
    val viewModel: AppViewModel =
        viewModel(factory = AppViewModelFactory(appContainer.userSettingsRepo))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            NavHost(navController = controller, startDestination = Route.MainRoute) {

                composable<Route.MainRoute> {
                    MainView(controller)
                }

                composable<Route.MapRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Route.MapRoute>()
                    val factory = remember {
                        MapViewModelFactory(
                            appContainer.weatherRepo,
                            appContainer.userSettingsRepo, args.mode
                        )
                    }
                    val viewModel: MapViewModel = viewModel(factory = factory)
                    MapView(
                        navController = controller,
                        viewModel = viewModel
                    )
                }

                composable<Route.FavWeatherRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Route.FavWeatherRoute>()
                    val factory = remember {
                        FavWeatherDisplayViewModelFactory(
                            appContainer.weatherRepo,
                            appContainer.userSettingsRepo
                        )
                    }
                    val viewModel: FavWeatherDisplayViewModel = viewModel(factory = factory)
                    FavWeatherDisplayView(
                        viewModel = viewModel,
                        bottomController = controller,
                        lat = args.lat,
                        lng = args.lng,
                        cityId = args.cityId,
                        onNavigateBack = { controller.popBackStack() }
                    )
                }
            }
        }
        NetworkBanner(viewModel)
    }
}

@Composable
fun NetworkBanner(appViewModel: AppViewModel) {
    val isConnected by appViewModel.isConnected.collectAsStateWithLifecycle()
    AnimatedVisibility(
        visible = !isConnected,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NoInternetBanner()
    }
}


@Composable
fun NoInternetBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red.copy(alpha = 0.9f))
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = "No Internet",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "No internet connection",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}