package com.justdance.apptesis.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.justdance.apptesis.R

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    val email: String by viewModel.emailText.observeAsState("")
    val pass: String by viewModel.passText.observeAsState("")
    val state = rememberScrollState(0)

    Surface {
        Column(modifier = Modifier.verticalScroll(state)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize().padding(top = 60.dp)) {
                Text(text = "App Tesis", style = MaterialTheme.typography.h2)
            }
            Spacer(modifier = Modifier.height(45.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Card {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp)) {
                        OutlinedTextField(
                            modifier = Modifier.padding(8.dp),
                            label = {
                                Text(text = "Correo")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            value = email,
                            onValueChange = {
                                    s -> viewModel.emailChanged(s)
                            })
                        Spacer(modifier = Modifier.height(25.dp))
                        OutlinedTextField(
                            modifier = Modifier.padding(8.dp),
                            label = {
                                Text(text = "Contraseña")
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            value = pass,
                            onValueChange = {
                                    s -> viewModel.passChanged(s)
                            })
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = { /*TODO*/ }, modifier = Modifier.width(280.dp)) {
                            Text(text = "Iniciar sesion")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 1.dp, 
                    color = MaterialTheme.colors.surface) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                       Text(text = "No estas registrado?") 
                        TextButton(onClick = { navController.navigate("register", NavOptions.Builder().setEnterAnim(
                            R.anim.nav_default_pop_enter_anim).build()) }) {
                            Text(text = "Registrate")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(45.dp))
        }
    }
}
