package com.justdance.apptesis.ui.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.justdance.apptesis.R

@ExperimentalAnimationApi
@Composable
fun Dropdown(items: List<String>, index: Int, onItemSelected: (index: Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(400))
    
    Surface(elevation = 1.dp, modifier = Modifier.padding(8.dp)) {
        Box {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .width(260.dp)
                .padding(8.dp)
                .clickable { expanded = true }) {
                Box(modifier = Modifier.fillMaxWidth(0.8f)) {
                    Text(items[index], style = MaterialTheme.typography.subtitle2, maxLines = 1)
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                        contentDescription = null,
                        Modifier.graphicsLayer {
                            rotationX = rotation
                        }.align(Alignment.CenterEnd))
                }
            }
            /*AnimatedVisibility(visible = expanded) {
                Spacer(modifier = Modifier.height(45.dp))
            }*/
            DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(MaterialTheme.colors.surface)
                ) {
                items.forEachIndexed { i, s ->
                    DropdownMenuItem(onClick = {
                        onItemSelected(i)
                        expanded = false
                    }) {
                        Text(text = s, style = MaterialTheme.typography.subtitle1)
                    }
                }
            }
        }
    }
}
