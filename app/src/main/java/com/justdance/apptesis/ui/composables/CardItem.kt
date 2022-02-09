package com.justdance.apptesis.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CardItem(title: String, info1: String, info2: String) {
    Card(elevation = 10.dp, modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text(title, style = MaterialTheme.typography.h3)
            Text(info1, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
            Text(info2, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
        }
    }
}
