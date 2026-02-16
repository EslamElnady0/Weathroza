package com.eslamdev.weathroza.presentaion.main.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.presentaion.main.model.BottomNavItem
import com.eslamdev.weathroza.presentaion.routes.BottomRoute

@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(
        BottomNavItem(
            route = BottomRoute.Home,
            icon = Icons.Filled.Home,
            label = R.string.home
        ),
        BottomNavItem(
            route = BottomRoute.Favourites,
            icon = Icons.Filled.Favorite,
            label = R.string.favourites
        ),
        BottomNavItem(
            route = BottomRoute.Alerts,
            icon = Icons.Filled.Notifications,
            label = R.string.alerts
        ),
        BottomNavItem(
            route = BottomRoute.Settings,
            icon = Icons.Filled.Settings,
            label = R.string.settings
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.route == item.route::class.qualifiedName

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.7f else 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "scale"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.label),
                        modifier = Modifier.scale(scale)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.label),
                        modifier = Modifier.scale(scale)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.primary,
                    selectedTextColor = AppColors.primary,
                    indicatorColor = AppColors.primary.copy(alpha = 0.1f)
                ),
                interactionSource = interactionSource
            )
        }
    }
}
