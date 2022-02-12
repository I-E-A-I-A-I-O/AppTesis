package com.justdance.apptesis.ui.composables

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.justdance.apptesis.R
import com.justdance.apptesis.ui.screens.home.HomeViewModel

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home_screen_id, Icons.Filled.Home)
    object Notifications : Screen("notifications", R.string.notifications_screen_id, Icons.Filled.Notifications)
    object Settings : Screen("settings", R.string.settings_screen_id, Icons.Filled.Settings)
    object Semesters : Screen("semesters", R.string.periods_screen_id, Icons.Filled.ListAlt)
    object Surveys : Screen("surveys", R.string.surveys_screen_id, Icons.Filled.Quiz)
}

private val teacherNavRoutes = listOf(
    Screen.Home,
    Screen.Surveys,
    Screen.Notifications,
    Screen.Settings
)

private val studentNavRoutes = listOf(
    Screen.Home,
    Screen.Surveys,
    Screen.Semesters,
    Screen.Notifications,
    Screen.Settings
)

@Composable
fun BottomNav(navController: NavHostController, viewModel: HomeViewModel) {
    val role by viewModel.role.observeAsState("not")

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getRole()
    })

    BottomNavigation {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination
        val routes: List<Screen> = when (role) {
            "teacher" -> teacherNavRoutes
            "student" -> studentNavRoutes
            else -> listOf()
        }

        routes.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = {
                    Text(
                        stringResource(screen.resourceId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    val currentRoute = currentDestination?.route
                    navController.navigate(screen.route) {
                        currentRoute?.let {
                            popUpTo(it) {
                                inclusive = true
                                //saveState = true
                            }
                        }
                        launchSingleTop = true
                        //restoreState = true
                    }
                })
        }
    }
}