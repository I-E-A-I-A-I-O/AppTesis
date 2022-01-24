package com.justdance.apptesis.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.justdance.apptesis.R
import com.justdance.apptesis.SnackActions

@ExperimentalComposeUiApi
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel,
                showMessage: (String, String?, SnackActions) -> Unit) {
    val ci: String by viewModel.ciText.observeAsState("")
    val pass: String by viewModel.passText.observeAsState("")
    val loading: Boolean by viewModel.isLoading.observeAsState(false)
    val (pField) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = rememberScrollState(0)
    var formValid by remember { mutableStateOf(false) }
    var passValid by remember { mutableStateOf(false) }
    var ciValid by remember { mutableStateOf(false) }
    val validateForm = {
        ciValid = ci.isNotEmpty() && ci.matches("^[0-9]*\$".toRegex())
        passValid = pass.isNotEmpty() && pass.length <= 30
        formValid = ciValid && passValid
    }
    val onLogin = {
        if (!formValid) {
            val snackText: String = if (!ciValid) {
                "Cedula invalida."
            } else if (!passValid) {
                "Contraseña vacia o my larga."
            } else {
                ""
            }
            showMessage(snackText, null, SnackActions.NONE)
        } else {

        }
    }

    Surface {
        Column(modifier = Modifier.verticalScroll(state)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)) {
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
                            modifier = Modifier
                                .width(290.dp)
                                .padding(8.dp),
                            singleLine = true,
                            isError = ciValid,
                            label = {
                                Text(text = "Cedula de identidad")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { pField.requestFocus() }
                            ),
                            value = ci,
                            onValueChange = {
                                    s ->
                                viewModel.ciChanged(s)
                                validateForm()
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .width(290.dp)
                                .padding(8.dp)
                                .focusRequester(pField),
                            singleLine = true,
                            isError = passValid,
                            label = {
                                Text(text = "Contraseña")
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    pField.freeFocus()
                                }
                            ),
                            value = pass,
                            onValueChange = {
                                    s ->
                                viewModel.passChanged(s)
                                validateForm()
                            })
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            enabled = !loading,
                            onClick = {
                                validateForm()
                                onLogin()
                            },
                            modifier = Modifier.width(280.dp)) {
                            Text(text = if (loading) "Iniciando sesion..." else "Iniciar sesion")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 1.dp, 
                    color = MaterialTheme.colors.surface) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                       Text(text = "No estas registrado?") 
                        TextButton(enabled = !loading,
                            onClick = { navController.navigate("register") }) {
                            Text(text = "Registrate")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(45.dp))
        }
    }
}
