package com.justdance.apptesis.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {
    Surface {
        Column {
            ListItem(Modifier.clickable {  }) {
                Text("Cerrar sesion")
            }
            Divider()
        }
    }
}