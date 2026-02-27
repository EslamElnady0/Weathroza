package com.eslamdev.weathroza.presentaion.favourite.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.components.AddFab
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.presentaion.favourite.view.components.FavEmptyState
import com.eslamdev.weathroza.presentaion.favourite.view.components.FavItem
import com.eslamdev.weathroza.presentaion.favourite.view.components.FavSearchBar
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModel

@Composable
fun FavView(
    bottomController: NavController,
    viewModel: FavViewModel,
    modifier: Modifier = Modifier,
    onNavigateToMap: () -> Unit,
    onNavigateToFavWeather: (lat: Double, lng: Double, cityId: Long) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            AddFab(
                onClick = onNavigateToMap,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            HeightSpacer(16.0)

            Text(
                text = stringResource(R.string.favourites),
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
                        Text(
                            text = stringResource(state.messageRes ?: R.string.error_unknown),
                            color = Color.Red
                        )
                    }
                }

                is UiState.Success -> {

                    val locations = state.data.filter {
                        val name =
                            remember { if (settings.language == AppLanguage.ARABIC) it.arName else it.enName }
                        name.contains(searchQuery, ignoreCase = true) ||
                                it.country.contains(searchQuery, ignoreCase = true)
                    }
                    if (locations.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            FavEmptyState()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(locations, key = { it.cityId }) { location ->
                                FavItem(
                                    location = location,
                                    settings = settings,
                                    onRemoveFavourite = viewModel::removeFavourite,
                                    onClick = {
                                        onNavigateToFavWeather(
                                            location.lat,
                                            location.lng,
                                            location.cityId
                                        )
                                    }
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