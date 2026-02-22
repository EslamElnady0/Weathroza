package com.eslamdev.weathroza.presentaion.favourite.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity

@Composable
fun FavLocationCard(
    location: FavouriteLocationEntity,
    weatherDesc: String = "SUNNY",
    iconRes: Int = R.drawable.dummy_sun_image,
    modifier: Modifier = Modifier
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = weatherDesc,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = weatherDesc,
                        color = Color(0xFFA0AAB2),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "${location.lastTemp?.toInt() ?: 0}°",
                    color = Color.White,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
