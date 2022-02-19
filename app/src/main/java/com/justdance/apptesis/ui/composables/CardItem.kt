package com.justdance.apptesis.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardItem(title: String, info1: String, info2: String, onClick: () -> Unit) {
    Card(elevation = 10.dp, modifier = Modifier.padding(8.dp), onClick = { onClick() }) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(title, style = MaterialTheme.typography.h3)
            Text(info1, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
            Text(info2, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
        }
    }
}

@Composable
fun CardItemWithButton(title: String, info1: String, info2: String, Icon: ImageVector, onClick: () -> Unit) {
    Card(elevation = 10.dp, modifier = Modifier.padding(8.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                /*.fillMaxWidth()*/
                .padding(8.dp)) {
                Text(title, style = MaterialTheme.typography.h3)
                Text(info1, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
                Text(info2, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                IconButton(onClick = { onClick() }) {
                    Icon(Icon, contentDescription = "", tint = Color.Green)
                }
            }
        }
    }
}
