package com.eslamdev.weathroza.presentaion.home.view.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.components.WidthSpacer
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun StatsRow(
    @DrawableRes icon: Int,
    label: String,
    value: String,
    contentDesc: String, modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painterResource(icon),
            contentDescription = contentDesc,
        )
        WidthSpacer(12.0)
        Column() {
            Text(label, color = AppColors.darkGray)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}