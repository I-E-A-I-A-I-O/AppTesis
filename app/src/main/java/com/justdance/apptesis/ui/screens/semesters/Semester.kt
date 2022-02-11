package com.justdance.apptesis.ui.screens.semesters

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.justdance.apptesis.ui.composables.CardItem
import com.justdance.apptesis.ui.screens.home.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SemesterScreen(navController: NavController, viewModel: HomeViewModel, id: String?) {
    val courses by viewModel.courses.observeAsState(listOf())
    val state = rememberLazyGridState()

    LaunchedEffect(key1 = Unit, block = {
        if (!id.isNullOrBlank()) {
            viewModel.getCourses(id)
        }
    })

    if (!id.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay materias para este semestre.", style = MaterialTheme.typography.h6)
        }
    } else {
        LazyVerticalGrid(cells = GridCells.Adaptive(195.dp), state = state) {
            items(courses) { course ->
                CardItem(title = course.name, info2 = "Seccion: ${course.group}", info1 = "") {

                }
            }
        }
    }
}