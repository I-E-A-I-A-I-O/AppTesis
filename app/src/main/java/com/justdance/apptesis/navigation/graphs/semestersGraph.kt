package com.justdance.apptesis.navigation.graphs

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.justdance.apptesis.R
import com.justdance.apptesis.SnackActions
import com.justdance.apptesis.ui.screens.home.HomeViewModel
import com.justdance.apptesis.ui.screens.semesters.AddCourseScreen
import com.justdance.apptesis.ui.screens.semesters.AddCourseViewModel
import com.justdance.apptesis.ui.screens.semesters.SemesterScreen
import com.justdance.apptesis.ui.screens.semesters.Semesters

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.semestersGraph(
    navHost: NavHostController,
    homeViewModel: HomeViewModel,
    addCourseViewModel: AddCourseViewModel,
    onSnack: (message: String, actionLabel: String?, action: SnackActions) -> Unit
) {
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
        composable(
            "add-course",
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700))
            }) {
            it.destination.label = stringResource(id = R.string.add_course_screen_id)
            AddCourseScreen(navHost = navHost, viewModel = addCourseViewModel, onSnack)
        }
    }
}