package com.eslamdev.weathroza.presentaion.favourite.view.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.DeleteDialog
import com.eslamdev.weathroza.core.components.SwipeToDeleteBox
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.UserSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavItem(
    location: FavouriteLocationEntity,
    settings: UserSettings,
    onRemoveFavourite: (Long) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        val name =
            remember { if (settings.language == AppLanguage.ARABIC) location.arName else location.enName }
        DeleteDialog(
            title = stringResource(R.string.delete_fav_title),
            description = stringResource(R.string.delete_fav_desc, name, location.country),
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onRemoveFavourite(location.cityId)
            },
        )
    }

    SwipeToDeleteBox(
        onSwipedToDelete = { showDeleteDialog = true },
        modifier = modifier,
    ) {
        FavLocationCard(
            location = location,
            settings = settings,
            modifier = Modifier.clickable { onClick() },
        )
    }
}
