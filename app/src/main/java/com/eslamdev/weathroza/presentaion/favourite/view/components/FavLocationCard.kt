package com.eslamdev.weathroza.presentaion.favourite.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.WidthSpacer
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavLocationCard(
    location: FavouriteLocationEntity,
    modifier: Modifier = Modifier,
    iconRes: Int = R.drawable.dummy_sun_image,
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
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location.name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = location.country,
                    color = Color(0xFFA0AAB2),
                    fontSize = 14.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                GlideImage(
                    model = location.iconUrl,
                    contentDescription = "weather status icon",
                    modifier = Modifier.size(50.dp)
                )

                WidthSpacer(10.0)
                Text(
                    text = "${location.lastTemp?.toInt() ?: 0}°",
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
