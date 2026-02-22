package com.eslamdev.weathroza.presentaion.favourite.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.presentaion.favourite.view.components.FavLocationCard
import com.eslamdev.weathroza.presentaion.favourite.view.components.FavSearchBar

@Composable
fun FavView(
    bottomController: NavController,
    modifier: Modifier = Modifier,
    onNavigateToMap: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    val dummyLocations = remember {
        listOf(
            FavouriteLocationEntity(
                cityId = 1,
                name = "London",
                country = "United Kingdom",
                lat = 51.5,
                lng = -0.12,
                lastTemp = 22.0,
                icon = "01d",
                iconUrl = "",
                lastUpdated = System.currentTimeMillis()
            ),
            FavouriteLocationEntity(
                cityId = 2,
                name = "Tokyo",
                country = "Japan",
                lat = 35.6,
                lng = 139.6,
                lastTemp = 18.0,
                icon = "03d",
                iconUrl = "",
                lastUpdated = System.currentTimeMillis()
            ),
            FavouriteLocationEntity(
                cityId = 3,
                name = "New York",
                country = "United States",
                lat = 40.7,
                lng = -74.0,
                lastTemp = 14.0,
                icon = "09d",
                iconUrl = "",
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToMap,
                shape = CircleShape,
                containerColor = Color(0xFF1CB0F6),
                contentColor = Color.Black,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Location",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            HeightSpacer(16.0)

            Text(
                text = "Favorites",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            HeightSpacer(24.0)
            FavSearchBar(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it }
            )

            HeightSpacer(24.0)

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(dummyLocations) { location ->
                    val weatherDesc = when (location.name) {
                        "London" -> "SUNNY"
                        "Tokyo" -> "CLOUDY"
                        else -> "RAINY"
                    }
                    val iconRes = when (location.name) {
                        "London" -> R.drawable.dummy_sun_image
                        "Tokyo" -> R.drawable.cloud_ic
                        else -> R.drawable.cloud_ic
                    }

                    FavLocationCard(
                        location = location,
                        weatherDesc = weatherDesc,
                        iconRes = iconRes
                    )
                    HeightSpacer(12.0)
                }

            }
        }
    }
}