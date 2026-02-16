package com.eslamdev.weathroza.presentaion.main.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.eslamdev.weathroza.presentaion.routes.BottomRoute

data class BottomNavItem(
    val route: BottomRoute,
    val icon: ImageVector,
    val label: Int
)
