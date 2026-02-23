package com.eslamdev.weathroza.presentaion.favourite.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity

@Composable
fun DeleteFavDialog(
    location: FavouriteLocationEntity,
    settings: UserSettings,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AppColors.error.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = AppColors.error,
                        modifier = Modifier.size(30.dp)
                    )
                }

                HeightSpacer(16.0)

                Text(
                    text = stringResource(R.string.delete_fav_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                HeightSpacer(10.0)

                val name = remember { if (settings.language == AppLanguage.ARABIC) location.arName else location.enName }
                Text(
                    text = stringResource(R.string.delete_fav_desc, name, location.country),
                    fontSize = 13.sp,
                    color = AppColors.lightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                HeightSpacer(24.0)

                HorizontalDivider(color = AppColors.lightGray.copy(alpha = 0.15f))

                HeightSpacer(5.0)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = AppColors.lightGray,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .width(1.dp)
                            .padding(vertical = 12.dp)
                            .background(AppColors.lightGray.copy(alpha = 0.15f))
                    )

                    TextButton(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.delete),
                            color = AppColors.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
