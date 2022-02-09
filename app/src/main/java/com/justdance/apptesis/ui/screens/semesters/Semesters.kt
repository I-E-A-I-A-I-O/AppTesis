package com.justdance.apptesis.ui.screens.semesters

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.justdance.apptesis.ui.screens.home.HomeViewModel

@Composable
fun Semesters(navController: NavController, viewModel: HomeViewModel) {
    val semesters by viewModel.semesters.observeAsState(listOf())
    val loading by viewModel.isLoading.observeAsState(false)

    LaunchedEffect(key1 = Unit, block = {
        viewModel.update()
    })

    Surface {
        semesters.forEach {
            Text(it.name)
        }
    }
}