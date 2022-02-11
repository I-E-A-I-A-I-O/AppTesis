package com.justdance.apptesis.ui.screens.start

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.justdance.apptesis.ui.composables.CenteredLoading

@ExperimentalPermissionsApi
@Composable
fun StartScreen(navController: NavController, viewModel: StartViewModel) {
    val context = LocalContext.current
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    )
    val onResponse = {
        m: String, s: Boolean ->

        if (s) {
            navController.navigate("homeNav") {
                popUpTo("start") {
                    inclusive = true
                }
            }
        } else {
            if (m.isNotEmpty()) {
                Toast.makeText(context, m, Toast.LENGTH_SHORT).show()
            }
            navController.navigate("login?redirected=false") {
                popUpTo("start") {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        if (locationPermissions.shouldShowRationale) {
            locationPermissions.launchMultiplePermissionRequest()
        }
        viewModel.verifySession(onResponse)
    })

    CenteredLoading()
}
