package com.justdance.apptesis.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.justdance.apptesis.R

enum class BottomSheetLayouts {
    Join
}

sealed class Option(@StringRes val resourceId: Int, val icon: ImageVector) {
    object Join: Option(R.string.join, Icons.Filled.GroupAdd)
    object Delete: Option(R.string.delete, Icons.Filled.Delete)
}

private val JoinLayout = listOf(
    Option.Join
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(Layout: BottomSheetLayouts, onOption: (index: Int) -> Unit) {
    Surface {
        when (Layout) {
            BottomSheetLayouts.Join -> {
                JoinLayout.forEach {
                    ListItem(
                        icon = { Icon(it.icon, contentDescription = "Unirse") },
                        modifier = Modifier.clickable { onOption(0) }
                    ) {
                        Text(stringResource(it.resourceId))
                    }
                }
            }
        }
    }
}