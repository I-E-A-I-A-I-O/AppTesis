package com.justdance.apptesis.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justdance.apptesis.R

class BottomSheetItem(
    val icon: Int,
    val title: String,
    val onItemClick: () -> Unit
)

@Composable
fun BottomSheetContent(sheetItems: List<BottomSheetItem>) {
    Column {
        for (item in sheetItems) {
            BottomSheetListItem(
                icon = item.icon,
                title = item.title,
                onItemClick = item.onItemClick
            )
        }
    }
}

@Composable
fun BottomSheetListItem(icon: Int, title: String, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick() })
            .height(55.dp)
            .padding(start = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = "BottomSheet option")
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = title)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetListItemPreview() {
    BottomSheetListItem(
        icon = R.drawable.googleg_standard_color_18,
        title = "Share",
        onItemClick = {  },
    )
}
