package com.eslamdev.weathroza.presentaion.alerts.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.presentaion.alerts.model.AlertItem

@Composable
fun AlertsHeader(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.alerts),
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = AppColors.white,
            fontSize = 28.sp,
        ),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
fun AlertCard(
    item: AlertItem,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    CardWithBoarder(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AlertCardIcon()
            AlertCardInfo(
                title = item.title,
                timeRange = item.timeRange,
                location = item.location,
                modifier = Modifier.weight(1f),
            )
            Switch(
                checked = item.isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = AppColors.primary,
                    checkedThumbColor = AppColors.white,
                    uncheckedTrackColor = AppColors.primary.copy(alpha = 0.2f),
                ),
            )
        }
    }
}

@Composable
private fun AlertCardIcon() {
    Box(
        modifier = Modifier
            .size(44.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = AppColors.primary,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun AlertCardInfo(
    title: String,
    timeRange: String,
    location: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = AppColors.white,
            ),
        )
        Text(
            text = timeRange,
            style = MaterialTheme.typography.bodySmall.copy(color = AppColors.lightGray),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = AppColors.lightGray,
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = location,
                style = MaterialTheme.typography.bodySmall.copy(color = AppColors.lightGray),
            )
        }
    }
}

@Composable
fun AlertsList(
    alerts: List<AlertItem>,
    onToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(alerts) { alert ->
            AlertCard(
                item = alert,
                onToggle = { enabled -> onToggle(alert.id, enabled) },
            )
        }
    }
}

@Composable
fun EmptyAlertsState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_alerts),
            style = MaterialTheme.typography.bodyLarge.copy(color = AppColors.lightGray),
        )
    }
}