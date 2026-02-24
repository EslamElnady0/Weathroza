package com.eslamdev.weathroza.presentaion.favourite.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.UserSettings

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavLocationCard(
    location: FavouriteLocationEntity,
    settings: UserSettings,
    modifier: Modifier = Modifier,
) {
    CardWithBoarder(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.6f)
            ) {
                val name =
                    remember { if (settings.language == AppLanguage.ARABIC) location.arName else location.enName }
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = location.country,
                    color = AppColors.lightGray,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    model = location.iconUrl,
                    contentDescription = "weather status icon",
                    modifier = Modifier.size(50.dp)
                )
            }

            Box(modifier = Modifier.weight(0.3f), contentAlignment = Alignment.CenterEnd) {
                DegreeText(
                    degree = location.lastTemp ?: 0.0,
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Light,
                    settings = settings
                )
            }

        }
    }
}
