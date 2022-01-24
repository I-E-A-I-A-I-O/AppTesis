package com.justdance.apptesis.screens.start

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun StartScreen(navController: NavController, viewModel: StartViewModel) {
    val context = LocalContext.current
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
        viewModel.verifySession(onResponse)
    })

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
