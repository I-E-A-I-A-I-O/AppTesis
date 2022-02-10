package com.justdance.apptesis.ui.screens.semesters

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.justdance.apptesis.ui.composables.CardItem
import com.justdance.apptesis.ui.screens.home.HomeViewModel

@Composable
fun SemesterScreen(navController: NavController, viewModel: HomeViewModel, id: String?) {
    val courses by viewModel.courses.observeAsState(listOf())
    val state = rememberLazyListState()

    LaunchedEffect(key1 = Unit, block = {
        if (!id.isNullOrBlank()) {
            viewModel.getCourses(id)
        }
    })

    if (id.isNullOrEmpty()) {
        Text("AAA")
    } else {
        Text("COUNT ${courses.count()}")
        LazyColumn(state = state) {
            items(courses) { course ->
                CardItem(title = course.name, info1 = "a", info2 = "b") {

                }
            }
        }
    }
}