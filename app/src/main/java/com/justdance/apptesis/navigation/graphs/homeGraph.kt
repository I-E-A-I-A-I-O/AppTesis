package com.justdance.apptesis.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.justdance.apptesis.R
import com.justdance.apptesis.ui.screens.home.HomeScreen
import com.justdance.apptesis.ui.screens.home.HomeViewModel
import com.justdance.apptesis.ui.screens.semesters.SemesterScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeGraph(navHost: NavHostController, homeViewModel: HomeViewModel) {
    navigation("home", "homeNav") {
        composable("home") {
            it.destination.label = stringResource(id = R.string.home_screen_id)
            HomeScreen(navHost, homeViewModel)
        }
        composable(
            "semester?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            backStackEntry.destination.label = stringResource(id = R.string.period_screen_id)
            SemesterScreen(navHost, homeViewModel, backStackEntry.arguments?.getString("id"))
        }
    }
}