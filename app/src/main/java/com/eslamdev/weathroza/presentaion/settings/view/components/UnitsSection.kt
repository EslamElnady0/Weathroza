package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun UnitsSection(
    modifier: Modifier = Modifier,
    selectedTempUnitIndex: Int,
    onTempOptionSelected: (Int) -> Unit,
    selectedWindUnitIndex: Int,
    onWindOptionSelected: (Int) -> Unit
) {
    CardWithBoarder(modifier = modifier) {
        Column {
            UnitSectionContent(
                title = "Temperature",
                icon = Icons.Default.DeviceThermostat,
                content = {
                    UnitSelector(
                        selectedOptionIndex = selectedTempUnitIndex,
                        onOptionSelected = onTempOptionSelected,
                        horizontalPadding = 8.dp
                    )
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = AppColors.primary.copy(alpha = 0.1f)
            )

            UnitSectionContent(
                title = "Wind Speed",
                icon = Icons.Default.Air,
                content = {
                    SettingsSelector(
                        horizontalPadding = 8.dp
                    ) {
                        SettingSelectorItem(
                            title = "m/s",
                            isSelected = selectedWindUnitIndex == 0,
                            onClick = { onWindOptionSelected(0) }
                        )
                        SettingSelectorItem(
                            title = "mph",
                            isSelected = selectedWindUnitIndex == 1,
                            onClick = { onWindOptionSelected(1) }
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun UnitSectionContent(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = AppColors.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AppColors.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        HeightSpacer(16.0)

        content()
    }
}
