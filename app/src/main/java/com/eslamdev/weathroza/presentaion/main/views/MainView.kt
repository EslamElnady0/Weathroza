package com.eslamdev.weathroza.presentaion.main.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eslamdev.weathroza.core.components.AppSnackbar
import com.eslamdev.weathroza.core.enums.MapMode
import com.eslamdev.weathroza.core.helpers.SnackbarController
import com.eslamdev.weathroza.core.helpers.SnackbarEvent
import com.eslamdev.weathroza.core.helpers.SnackbarType
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.AlertsViewModel
import com.eslamdev.weathroza.presentaion.alerts.views.AlertsView
import com.eslamdev.weathroza.presentaion.favourite.view.FavView
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModel
import com.eslamdev.weathroza.presentaion.home.view.HomeBody
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModel
import com.eslamdev.weathroza.presentaion.main.components.BottomNavBar
import com.eslamdev.weathroza.presentaion.routes.BottomRoute
import com.eslamdev.weathroza.presentaion.routes.Route
import com.eslamdev.weathroza.presentaion.settings.view.SettingsView
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainView(
    controller: NavController,
    modifier: Modifier = Modifier
) {
    val bottomController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var currentEvent by remember { mutableStateOf<SnackbarEvent?>(null) }

    LaunchedEffect(Unit) {
        SnackbarController.events.collect { event ->
            currentEvent = event
            snackbarHostState.showSnackbar(message = " ")
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(bottomController) },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    AppSnackbar(
                        snackbarData = snackbarData,
                        currentEvent = currentEvent,
                        type = currentEvent?.type ?: SnackbarType.SUCCESS
                    )
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = bottomController,
            startDestination = BottomRoute.Home,
            modifier = Modifier.padding(padding)
        ) {
            composable<BottomRoute.Home> {
                val viewModel: HomeViewModel = koinViewModel()
                HomeBody(
                    bottomController = controller,
                    viewModel = viewModel,
                    onNavigateToSettings = {
                        bottomController.navigate(BottomRoute.Settings) {
                            popUpTo(bottomController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable<BottomRoute.Favourites> {
                val viewModel: FavViewModel = koinViewModel()
                FavView(
                    bottomController = bottomController,
                    viewModel = viewModel,
                    onNavigateToMap = { controller.navigate(Route.MapRoute(MapMode.ADD_FAVOURITE)) },
                    onNavigateToFavWeather = { lat, lng, cityId ->
                        controller.navigate(Route.FavWeatherRoute(lat, lng, cityId))
                    }
                )
            }
            composable<BottomRoute.Alerts> {
                val viewModel: AlertsViewModel = koinViewModel()
                AlertsView(bottomController, viewModel)
            }
            composable<BottomRoute.Settings> {
                val settingsViewModel: SettingsViewModel = koinViewModel()
                SettingsView(
                    bottomController = bottomController,
                    settingsViewModel = settingsViewModel,
                ) { controller.navigate(Route.MapRoute(MapMode.SELECT_LOCATION)) }
            }
        }
    }
}