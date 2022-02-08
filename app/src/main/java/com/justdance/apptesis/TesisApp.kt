package com.justdance.apptesis

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.justdance.apptesis.ui.screens.home.HomeScreen
import com.justdance.apptesis.ui.screens.home.HomeViewModel
import com.justdance.apptesis.ui.screens.login.LoginScreen
import com.justdance.apptesis.ui.screens.login.LoginViewModel
import com.justdance.apptesis.ui.screens.register.RegisterScreen
import com.justdance.apptesis.ui.screens.register.RegisterViewModel
import com.justdance.apptesis.ui.screens.start.StartScreen
import com.justdance.apptesis.ui.screens.start.StartViewModel
import com.justdance.apptesis.services.LocationService
import com.justdance.apptesis.ui.theme.AppTesisTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class SnackActions {
    NONE,
    REGISTER_GOTO_LOGIN
}

class MainActivity : ComponentActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private val registerViewModel by viewModels<RegisterViewModel>()
    private val startViewModel by viewModels<StartViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var scope: CoroutineScope
    private lateinit var scaffoldState: ScaffoldState
    private lateinit var navHost: NavHostController

    sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
        object Home : Screen("home", R.string.home_screen_id, Icons.Filled.Home)
    }

    val bottomNavRoutes = listOf(
        Screen.Home
    )

    @ExperimentalPermissionsApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, LocationService::class.java)
        startService(intent)
        setContent {
            TesisApp()
        }
    }

    @ExperimentalPermissionsApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @Composable
    fun TesisApp() {
        navHost = rememberNavController()
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()
        val backStackEntry by navHost.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination

        AppTesisTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = { if (BottomNavRoute(currentDestination?.route)) BottomNav(navHost) },
                topBar = { if (TopBarRoute(currentDestination?.route)) TopAppBar(title = { Text(text = currentDestination?.label.toString())}) }
            ) {
                NavHost(navController = navHost, startDestination = "identification") {
                    navigation(startDestination = "start", route = "identification") {
                        composable("start") {
                            StartScreen(navController = navHost, viewModel = startViewModel)
                        }
                        composable(
                            "login?redirected={redirected}",
                            arguments = listOf(navArgument("redirected") {defaultValue = false; type = NavType.BoolType})
                        ) { backStackEntry ->
                            backStackEntry.arguments?.let {
                                if (it.getBoolean("redirected")) {
                                    loginViewModel.ciChanged(registerViewModel.ciText.value!!)
                                    loginViewModel.passChanged(registerViewModel.passText.value!!)
                                }
                            }
                            LoginScreen(navController = navHost, viewModel = loginViewModel) {
                                    message, actionLabel, action: SnackActions ->
                                onSnack(message, actionLabel, action)
                            }
                        }
                        composable("register") {
                            RegisterScreen(navController = navHost, viewModel = registerViewModel) {
                                    message, actionLabel, action: SnackActions ->
                                onSnack(message, actionLabel, action)
                            }
                        }
                    }
                    navigation("home", "homeNav") {
                        composable("home") {
                            it.destination.label = stringResource(id = R.string.home_screen_id)
                            HomeScreen(navController = navHost, viewModel = homeViewModel)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomNav(navController: NavHostController) {
        BottomNavigation {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            bottomNavRoutes.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }

    private fun onSnack(message: String, actionLabel: String?, action: SnackActions) {
        scope.launch {
            val result = scaffoldState.snackbarHostState.showSnackbar(message, actionLabel)
            if (result == SnackbarResult.Dismissed) return@launch
            when (action) {
                SnackActions.NONE -> {}
                SnackActions.REGISTER_GOTO_LOGIN -> {
                    navHost.navigate("login?redirected=true") {
                        popUpTo("login?redirected={redirected}") {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    fun TopBarRoute(route: String?): Boolean {
        return when (route) {
            null -> false
            "start" -> false
            "login?redirected={redirected}" -> false
            "register" -> false
            else -> true
        }
    }

    fun BottomNavRoute(route: String?): Boolean {
        return when (route) {
            null -> false
            "start" -> false
            "login?redirected={redirected}" -> false
            "register" -> false
            else -> true
        }
    }
}
