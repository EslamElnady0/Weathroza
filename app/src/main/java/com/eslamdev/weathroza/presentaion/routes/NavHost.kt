package com.eslamdev.weathroza.presentaion.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eslamdev.weathroza.presentaion.main.views.MainView
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel

@Composable
fun App(settingsViewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val controller = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = controller, startDestination = Route.MainRoute) {
        composable<Route.MainRoute> {
            MainView(controller,settingsViewModel)
        }
    }
}

