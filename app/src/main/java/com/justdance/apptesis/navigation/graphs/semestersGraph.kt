package com.justdance.apptesis.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.justdance.apptesis.R
import com.justdance.apptesis.ui.screens.home.HomeViewModel
import com.justdance.apptesis.ui.screens.semesters.SemesterScreen
import com.justdance.apptesis.ui.screens.semesters.Semesters

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.semestersGraph(navHost: NavHostController, homeViewModel: HomeViewModel) {
    navigation("semesters", "semestersNav") {
        composable("semesters") {
            it.destination.label = stringResource(id = R.string.periods_screen_id)
            Semesters(navHost, homeViewModel)
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