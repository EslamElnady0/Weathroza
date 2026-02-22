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
import com.eslamdev.weathroza.core.enums.MapMode
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.presentaion.alerts.views.AlertsView
import com.eslamdev.weathroza.presentaion.favourite.view.FavView
import com.eslamdev.weathroza.presentaion.home.view.HomeBody
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModel
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModelFactory
import com.eslamdev.weathroza.presentaion.main.components.BottomNavBar
import com.eslamdev.weathroza.presentaion.routes.BottomRoute
import com.eslamdev.weathroza.presentaion.routes.Route
import com.eslamdev.weathroza.presentaion.settings.view.SettingsView
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModelFactory
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModel
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModelFactory

@Composable
fun MainView(
    controller: NavController,
    modifier: Modifier = Modifier
) {

    val bottomController = rememberNavController()

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
                val context = LocalContext.current
                val factory = remember { HomeViewModelFactory(WeatherRepo(context), context) }
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
                val context = LocalContext.current
                val factory = remember { FavViewModelFactory(WeatherRepo(context)) }
                val viewModel: FavViewModel = viewModel(factory = factory)
                FavView(
                    bottomController = bottomController,
                    viewModel = viewModel
                ) {
                    controller.navigate(Route.MapRoute(MapMode.ADD_FAVOURITE))
                }
            }

            composable<BottomRoute.Alerts> {
                AlertsView(bottomController)
            }

            composable<BottomRoute.Settings> {
                val context = LocalContext.current
                val settingsViewModel =
                    viewModel<SettingsViewModel>(factory = SettingsViewModelFactory(context))
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

//
//@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun HomeBodyPreview(modifier: Modifier = Modifier) {
//    MaterialTheme(colorScheme = darkColorScheme()) {
//        MainView(controller = rememberNavController())
//    }
//}