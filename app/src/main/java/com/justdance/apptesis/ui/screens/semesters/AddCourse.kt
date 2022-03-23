package com.justdance.apptesis.ui.screens.semesters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.justdance.apptesis.R
import com.justdance.apptesis.SnackActions
import com.justdance.apptesis.ui.composables.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddCourseScreen(
    navHost: NavHostController,
    viewModel: AddCourseViewModel,
    onSnack: (message: String, actionLabel: String?, action: SnackActions) -> Unit
) {
    val courses by viewModel.courses.observeAsState(listOf())
    val state = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val selectedId = remember { mutableStateOf("") }
    val onJoinCourse = { id: String ->
        if (id.isNotEmpty()) {

        }
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getCourses()
    })

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(
                sheetItems = listOf(
                    BottomSheetItem(
                        icon = R.drawable.ic_baseline_group_add_24,
                        title = stringResource(R.string.join),
                        onItemClick = { onJoinCourse(selectedId.value) }
                    ),
                    BottomSheetItem(
                        icon = R.drawable.ic_baseline_close_24,
                        title = stringResource(R.string.cancel),
                        onItemClick = { scope.launch { bottomSheetState.hide() } }
                    ),
                )
            )
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = bottomSheetState
    ) {
        if (courses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay materias disponibles.", style = MaterialTheme.typography.h6)
            }
        }
        else {
            LazyColumn(state = state) {
                items(courses) { item ->
                    CardItem(title = item.name, info1 = "Seccion ${item.group}", info2 = "") {
                        scope.launch {
                            selectedId.value = item.id
                            bottomSheetState.show()
                        }
                    }
                }
            }
        }
    }
}