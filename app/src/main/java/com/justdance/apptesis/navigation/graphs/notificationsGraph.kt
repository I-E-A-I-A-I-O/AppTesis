package com.justdance.apptesis.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.justdance.apptesis.R
import com.justdance.apptesis.ui.screens.notifications.NotificationsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.notificationsGraph(navHost: NavHostController) {
    navigation("notifications", "notificationsNav") {
        composable("notifications") {
            it.destination.label = stringResource(id = R.string.notifications_screen_id)
            NotificationsScreen(navController = navHost)
        }
    }
}