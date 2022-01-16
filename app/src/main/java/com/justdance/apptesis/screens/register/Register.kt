package com.justdance.apptesis.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.justdance.apptesis.R

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel) {
    Surface {
        Column() {
            IconButton(onClick = { navController.popBackStack() }, Modifier.padding(8.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = "Atras")
            }
            Card() {
                Column() {
                    OutlinedTextField(value = "", onValueChange = {} )
                }
            }
        }
    }
}
