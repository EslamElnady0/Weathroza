package com.eslamdev.weathroza.presentaion.main.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eslamdev.weathroza.WeathrozaApp
import com.eslamdev.weathroza.core.enums.MapMode
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.AlertViewModelFactory
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.AlertsViewModel
import com.eslamdev.weathroza.presentaion.alerts.views.AlertsView
import com.eslamdev.weathroza.presentaion.favourite.view.FavView
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModel
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModelFactory
import com.eslamdev.weathroza.presentaion.home.view.HomeBody
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModel
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModelFactory
import com.eslamdev.weathroza.presentaion.main.components.BottomNavBar
import com.eslamdev.weathroza.presentaion.routes.BottomRoute
import com.eslamdev.weathroza.presentaion.routes.Route
import com.eslamdev.weathroza.presentaion.settings.view.SettingsView
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModelFactory

@Composable
fun MainView(
    controller: NavController,
    modifier: Modifier = Modifier
) {

    val bottomController = rememberNavController()
    val context = LocalContext.current
    val appContainer = remember {
        (context.applicationContext as WeathrozaApp).appContainer
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(bottomController)
        }
    ) { padding ->

        NavHost(
            navController = bottomController,
            startDestination = BottomRoute.Home,
            modifier = Modifier.padding(padding)
        ) {

            composable<BottomRoute.Home> {
                val factory = remember {
                    HomeViewModelFactory(
                        appContainer.weatherRepo,
                        appContainer.userSettingsRepo
                    )
                }
                val viewModel: HomeViewModel = viewModel(
                    factory = factory
                )
                HomeBody(
                    bottomController = controller,
                    viewModel = viewModel,
                    onNavigateToSettings = {
                        bottomController.navigate(BottomRoute.Settings) {
                            popUpTo(bottomController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true

                        }
                    }
                )
            }

            composable<BottomRoute.Favourites> {
                val factory = remember {
                    FavViewModelFactory(
                        appContainer.weatherRepo,
                        appContainer.userSettingsRepo
                    )
                }
                val viewModel: FavViewModel = viewModel(factory = factory)
                FavView(
                    bottomController = bottomController,
                    viewModel = viewModel,
                    onNavigateToMap = {
                        controller.navigate(Route.MapRoute(MapMode.ADD_FAVOURITE))
                    },
                    onNavigateToFavWeather = { lat, lng, cityId ->
                        controller.navigate(Route.FavWeatherRoute(lat, lng, cityId))
                    }
                )
            }

            composable<BottomRoute.Alerts> {
                val factory = remember {
                    AlertViewModelFactory(
                        appContainer.userSettingsRepo,
                        appContainer.weatherRepo
                    )
                }
                val viewModel = viewModel<AlertsViewModel>(factory = factory)
                AlertsView(bottomController, viewModel)
            }

            composable<BottomRoute.Settings> {
                val settingsViewModel =
                    viewModel<SettingsViewModel>(factory = SettingsViewModelFactory(appContainer.userSettingsRepo))
                SettingsView(
                    bottomController = bottomController,
                    settingsViewModel,
                ) {
                    controller.navigate(Route.MapRoute(MapMode.SELECT_LOCATION))
                }
            }
        }
    }
}
