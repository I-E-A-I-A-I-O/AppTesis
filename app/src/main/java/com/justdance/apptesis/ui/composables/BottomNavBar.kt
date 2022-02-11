package com.justdance.apptesis.ui.composables

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.justdance.apptesis.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home_screen_id, Icons.Filled.Home)
    object Notifications : Screen("notifications", R.string.notifications_screen_id, Icons.Filled.Notifications)
    object Settings : Screen("settings", R.string.settings_screen_id, Icons.Filled.Settings)
}

private val bottomNavRoutes = listOf(
    Screen.Home,
    Screen.Notifications,
    Screen.Settings
)

@Composable
fun BottomNav(navController: NavHostController) {
    BottomNavigation {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination
        bottomNavRoutes.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
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