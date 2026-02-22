package com.eslamdev.weathroza.presentaion.favourite.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.presentaion.favourite.view.components.FavLocationCard
import com.eslamdev.weathroza.presentaion.favourite.view.components.FavSearchBar
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModel

@Composable
fun FavView(
    bottomController: NavController,
    viewModel: FavViewModel,
    modifier: Modifier = Modifier,
    onNavigateToMap: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }



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

            val uiState by viewModel.favourites.collectAsState()

            when (val state = uiState) {
                is UiState.Loading, UiState.Idle -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = Color.Red)
                    }
                }

                is UiState.Success -> {
                    val locations = state.data.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.country.contains(searchQuery, ignoreCase = true)
                    }
                    if (locations.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No saved locations", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(locations, key = { it.cityId }) { location ->
                                FavLocationCard(
                                    location = location,
                                    iconRes = R.drawable.dummy_sun_image
                                )
                                HeightSpacer(12.0)
                            }
                        }
                    }
                }
            }
        }
    }
}