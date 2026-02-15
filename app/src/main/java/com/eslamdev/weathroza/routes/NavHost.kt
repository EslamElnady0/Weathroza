package com.eslamdev.weathroza.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun App(modifier: Modifier = Modifier) {
    val controller = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = controller, startDestination = Route.HomeScreen) {
        composable<Route.HomeScreen> {
           // HomeScreen(controller = controller)
        }

    }
}

