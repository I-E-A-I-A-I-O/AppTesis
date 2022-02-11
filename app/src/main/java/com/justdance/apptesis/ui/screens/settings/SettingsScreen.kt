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
    val onLogout = {
        viewModel.clearDatabase()
        navController.navigate("identification") {
            popUpTo("settings") {
                inclusive = true
            }
        }
    }

    Surface {
        Column {
            ListItem(Modifier.clickable { onLogout() }) {
                Text("Cerrar sesion")
            }
            Divider()
        }
    }
}