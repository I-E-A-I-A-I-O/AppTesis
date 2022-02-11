package com.justdance.apptesis.ui.screens.semesters

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.justdance.apptesis.room.entities.Semesters
import com.justdance.apptesis.ui.composables.CardItem
import com.justdance.apptesis.ui.screens.home.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Semesters(navController: NavController, viewModel: HomeViewModel) {
    val semesters by viewModel.semesters.observeAsState(listOf())
    val loading by viewModel.isLoading.observeAsState(false)
    val state = rememberLazyGridState()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.update()
    })

    Surface {
        LazyVerticalGrid(cells = GridCells.Fixed(2), state = state) {
            items(semesters) { item ->
                CardItem(title = item.name, info1 = "from: " + item.from, info2 = "to: " + item.to) {
                    navController.navigate("semester?id=${item.id}")
                }
            }
            item {
                Spacer(Modifier.height(60.dp))
            }
        }
    }
}