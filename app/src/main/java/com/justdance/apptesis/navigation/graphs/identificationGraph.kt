package com.justdance.apptesis.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.justdance.apptesis.SnackActions
import com.justdance.apptesis.ui.screens.login.LoginScreen
import com.justdance.apptesis.ui.screens.login.LoginViewModel
import com.justdance.apptesis.ui.screens.register.RegisterViewModel
import com.justdance.apptesis.ui.screens.start.StartScreen
import com.justdance.apptesis.ui.screens.start.StartViewModel

@OptIn(
    ExperimentalAnimationApi::class,
    com.google.accompanist.permissions.ExperimentalPermissionsApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
fun NavGraphBuilder.identificationGraph(
    navHost: NavHostController,
    registerViewModel: RegisterViewModel,
    loginViewModel: LoginViewModel,
    startViewModel: StartViewModel,
    onSnack: (String, String?, SnackActions) -> Unit
) {
    navigation(startDestination = "start", route = "identification") {
        composable("start") {
            StartScreen(navController = navHost, viewModel = startViewModel)
        }
        composable(
            "login?redirected={redirected}",
            arguments = listOf(
                navArgument("redirected") {
                    defaultValue = false
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                if (it.getBoolean("redirected")) {
                    loginViewModel.ciChanged(registerViewModel.ciText.value!!)
                    loginViewModel.passChanged(registerViewModel.passText.value!!)
                }
            }
            LoginScreen(navHost, loginViewModel, onSnack)
        }
    }
}