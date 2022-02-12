package com.justdance.apptesis.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.justdance.apptesis.R
import com.justdance.apptesis.ui.screens.settings.SettingsScreen
import com.justdance.apptesis.ui.screens.settings.SettingsViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsGraph(navHost: NavHostController, settingsViewModel: SettingsViewModel) {
    navigation("settings", "settingsNav") {
        composable("settings") {
            it.destination.label = stringResource(id = R.string.settings_screen_id)
            SettingsScreen(navController = navHost, viewModel = settingsViewModel)
        }
    }
}
