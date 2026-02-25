package com.eslamdev.weathroza.presentaion.alerts.views.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.usersettings.UserSettings

@Composable
fun AlertsBody(
    uiState: UiState<List<AlertEntity>>,
    onToggle: (Long, Boolean) -> Unit,
    onDelete: (Long) -> Unit,
    settings: UserSettings,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is UiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppColors.primary)
            }
        }

        is UiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message ?: stringResource(R.string.error_unknown),
                    color = Color.Red,
                )
            }
        }

        is UiState.Success -> {
            if (uiState.data.isEmpty()) {
                EmptyAlertsState(modifier = modifier.fillMaxSize())
            } else {
                AlertsList(
                    alerts = uiState.data,
                    onToggle = onToggle,
                    onDelete = onDelete,
                    modifier = modifier,
                    settings = settings
                )
            }
        }

        UiState.Idle -> Unit
    }
}
