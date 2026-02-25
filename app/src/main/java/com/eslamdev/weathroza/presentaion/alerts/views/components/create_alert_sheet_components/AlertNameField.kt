package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun AlertNameField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.alert_name_hint),
                color = AppColors.lightGray,
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.primary,
            unfocusedBorderColor = AppColors.primary.copy(alpha = 0.3f),
            focusedTextColor = AppColors.white,
            unfocusedTextColor = AppColors.white,
            cursorColor = AppColors.primary,
            focusedContainerColor = AppColors.primary.copy(alpha = 0.05f),
            unfocusedContainerColor = AppColors.primary.copy(alpha = 0.05f),
        ),
    )
}
