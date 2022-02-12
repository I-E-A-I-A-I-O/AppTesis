package com.justdance.apptesis.ui.screens.home

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.justdance.apptesis.ui.composables.CenteredLoading
import com.justdance.apptesis.ui.screens.semesters.Semesters

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val role by viewModel.role.observeAsState("not")

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getRole()
    })

    when(role) {
        "teacher" -> Semesters(navController = navController, viewModel = viewModel)
        else -> CenteredLoading()
    }
}
