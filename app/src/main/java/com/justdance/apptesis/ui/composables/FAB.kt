package com.justdance.apptesis.ui.composables

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun FAB(navHost: NavHostController) {
    FloatingActionButton(onClick = { navHost.navigate("add-course") {
        this.anim { this.exit }
    }
    }) {
        Icon(Icons.Filled.Add, contentDescription = "Añadir materia")
    }
}