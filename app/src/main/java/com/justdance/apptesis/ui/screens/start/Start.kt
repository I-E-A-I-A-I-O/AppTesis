package com.justdance.apptesis.ui.screens.start

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

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
        if (locationPermissions.shouldShowRationale && !locationPermissions.permissionRequested) {
            locationPermissions.launchMultiplePermissionRequest()
        }
        viewModel.verifySession(onResponse)
    })

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
