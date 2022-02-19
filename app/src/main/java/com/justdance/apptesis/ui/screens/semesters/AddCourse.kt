package com.justdance.apptesis.ui.screens.semesters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.justdance.apptesis.SnackActions
import com.justdance.apptesis.ui.composables.CardItemWithButton

@Composable
fun AddCourseScreen(
    navHost: NavHostController,
    viewModel: AddCourseViewModel,
    onSnack: (message: String, actionLabel: String?, action: SnackActions) -> Unit
) {
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
                    CardItemWithButton(title = item.name, info1 = "Seccion ${item.group}", info2 = "", Icon = Icons.Filled.GroupAdd) {
                        onSnack("Creando peticion...", null, SnackActions.NONE)
                    }
                }
            }
        }
    }
}