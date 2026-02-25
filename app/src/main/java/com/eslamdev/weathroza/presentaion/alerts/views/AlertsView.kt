package com.eslamdev.weathroza.presentaion.alerts.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eslamdev.weathroza.core.components.AddFab
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.components.SettingsSelector
import com.eslamdev.weathroza.presentaion.alerts.model.AlertItem
import com.eslamdev.weathroza.presentaion.alerts.views.components.AlertTab
import com.eslamdev.weathroza.presentaion.alerts.views.components.AlertsHeader
import com.eslamdev.weathroza.presentaion.alerts.views.components.ScheduledSelectorItem
import com.eslamdev.weathroza.presentaion.alerts.views.components.ScheduledTabBody
import com.eslamdev.weathroza.presentaion.alerts.views.components.WeatherSelectorItem
import com.eslamdev.weathroza.presentaion.alerts.views.components.WeatherTabBody

@Composable
fun AlertsView(bottomController: NavController, modifier: Modifier = Modifier) {

    var selectedTab by remember { mutableStateOf(AlertTab.SCHEDULED) }

    var scheduledAlerts by remember {
        mutableStateOf(
            listOf(
                AlertItem(1, "Morning Briefing", "08:00 AM - 09:30 AM", "New York, NY", true),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
                AlertItem(2, "Commute Update", "05:00 PM - 06:30 PM", "Brooklyn, NY", false),
            )
        )
    }
    var weatherAlerts by remember { mutableStateOf(emptyList<AlertItem>()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            AddFab(
                onClick = { /* navigate to add-alert screen */ },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HeightSpacer(16.0)

            AlertsHeader()

            HeightSpacer(24.0)

            SettingsSelector(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalPadding = 8.dp
            ) {
                ScheduledSelectorItem(
                    isSelected = selectedTab == AlertTab.SCHEDULED,
                    onClick = { selectedTab = AlertTab.SCHEDULED },
                )
                WeatherSelectorItem(
                    isSelected = selectedTab == AlertTab.WEATHER,
                    onClick = { selectedTab = AlertTab.WEATHER },
                )
            }

            HeightSpacer(20.0)

            when (selectedTab) {
                AlertTab.SCHEDULED -> ScheduledTabBody(
                    alerts = scheduledAlerts,
                    onToggle = { id, enabled ->
                        scheduledAlerts = scheduledAlerts.map {
                            if (it.id == id) it.copy(isEnabled = enabled) else it
                        }
                    },
                )

                AlertTab.WEATHER -> WeatherTabBody(
                    alerts = weatherAlerts,
                    onToggle = { id, enabled ->
                        weatherAlerts = weatherAlerts.map {
                            if (it.id == id) it.copy(isEnabled = enabled) else it
                        }
                    },
                )
            }
        }
    }
}