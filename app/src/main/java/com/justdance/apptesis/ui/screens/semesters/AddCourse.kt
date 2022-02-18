package com.justdance.apptesis.ui.screens.semesters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.justdance.apptesis.ui.composables.CardItem

@Composable
fun AddCourseScreen(navHost: NavHostController, viewModel: AddCourseViewModel) {
    val courses by viewModel.courses.observeAsState(listOf())
    val state = rememberLazyListState()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getCourses()
    })

    Surface {
        if (courses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay materias disponibles.", style = MaterialTheme.typography.h6)
            }
        }
        else {
            LazyColumn(state = state) {
                items(courses) { item ->
                    CardItem(title = item.name, info1 = "x", info2 = "d") {

                    }
                }
            }
        }
    }
}