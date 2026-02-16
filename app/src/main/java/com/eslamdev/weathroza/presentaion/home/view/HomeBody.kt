package com.eslamdev.weathroza.presentaion.home.view

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.components.WidthSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.presentaion.home.view.components.StatusItem

@Composable
fun HomeBody(bottomController: NavController, modifier: Modifier = Modifier) {
    Column() {
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeBodyPreview(modifier: Modifier = Modifier) {

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold() { innerPadding ->
            Column(
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeightSpacer(48.0)
                Text("Miami", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                HeightSpacer(2.0)
                Text(
                    "Monday, Oct 23" + " • " + "10:00 AM",
                    color = AppColors.lightGray
                )
                Image(
                    painter = painterResource(id = R.drawable.dummy_sun_image),
                    contentDescription = "weather icon",
                    modifier = Modifier.height(130.dp)
                )
                Text(
                    "Clear Sky", fontSize = 20.sp, fontWeight = FontWeight.Medium,
                    color = AppColors.gray
                )
                DegreeText(
                    "75", fontSize = 96.sp,
                    color = AppColors.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                HeightSpacer(30.0)
                Row {
                    StatusItem(
                        icon = R.drawable.humidity_ic,
                        label = "Humidity",
                        value = "40%",
                        contentDesc = "humidity icon",
                        modifier = Modifier.weight(1f)
                    )
                    WidthSpacer(8.0)
                    StatusItem(
                        icon = R.drawable.wind_ic,
                        label = "Wind",
                        value = "5mph",
                        contentDesc = "wind icon",
                        modifier = Modifier.weight(1f)
                    )
                }
                HeightSpacer(8.0)
                Row {
                    StatusItem(
                        icon = R.drawable.pressure,
                        label = "Pressure",
                        value = "1012",
                        contentDesc = "pressure icon",
                        modifier = Modifier.weight(1f)
                    )
                    WidthSpacer(8.0)
                    StatusItem(
                        icon = R.drawable.cloud_ic,
                        label = "Clouds",
                        value = "12%",
                        contentDesc = "clouds icon",
                        modifier = Modifier.weight(1f)
                    )
                }

            }
        }
    }

}