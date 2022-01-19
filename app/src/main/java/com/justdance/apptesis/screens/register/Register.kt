package com.justdance.apptesis.screens.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.justdance.apptesis.R
import com.justdance.apptesis.ui.composables.Dropdown
import com.justdance.apptesis.utils.isValidEmail
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel, showMessage: (String) -> Unit) {
    val items = listOf("Carrera", "Ingenieria en Computacion", "Ingenieria Quimica", "Ingenieria Electrica",
        "Ingenieria en Telecomunicaciones", "Ingenieria Industrial")
    val email: String by viewModel.emailText.observeAsState("")
    val pass: String by viewModel.passText.observeAsState("")
    val pass2: String by viewModel.confirmPassText.observeAsState(initial = "")
    val name: String by viewModel.nameText.observeAsState("")
    val isLoading: Boolean by viewModel.isLoading.observeAsState(false)
    var selectedIndex by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState(0)
    var emailValid by remember { mutableStateOf(false) }
    val emailChanged = { s: String -> viewModel.emailChanged(s) }
    val nameUpdated = { s: String -> viewModel.nameChanged(s) }
    var nameValid by remember { mutableStateOf(false) }
    val passwordUpdated = { s: String -> viewModel.passChanged(s) }
    var passwordValid by remember { mutableStateOf(false) }
    var confirmValid by remember { mutableStateOf(false) }
    val confirmUpdated = { s: String -> viewModel.confirmPassChanged(s) }
    var indexValid by remember { mutableStateOf(false) }
    var formValid by remember { mutableStateOf(false) }
    val onButtonPressed = {
        if (!formValid) {
            val snackText: String = if (!nameValid) {
                "Nombre vacio o muy largo."
            } else if (!emailValid) {
                "Correo invalido."
            } else if (!indexValid) {
                "Seleccione una carrera."
            } else if (!passwordValid) {
                "Contraseña vacia o muy larga."
            } else if (!confirmValid) {
                "Las contraseñas no coinciden."
            } else {
                ""
            }
            showMessage(snackText)
        }
    }

    Surface {
        Column(Modifier.verticalScroll(scrollState)) {
            Spacer(modifier = Modifier.height(5.dp))
            IconButton(onClick = { navController.popBackStack() }, Modifier.padding(8.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = "Atras")
            }
            Spacer(modifier = Modifier.height(60.dp))
            RegisterForm(
                items = items,
                email = email,
                password = pass,
                confirm = pass2,
                name = name,
                selectedIndex = selectedIndex,
                onEmailUpdated = emailChanged,
                emailValid = emailValid,
                onNameUpdate = nameUpdated,
                nameValid = nameValid,
                passwordValid = passwordValid,
                onPasswordUpdated = passwordUpdated,
                confirmValid = confirmValid,
                onConfirmUpdated = confirmUpdated,
                indexValid = indexValid,
                formValid = formValid,
                loading = isLoading,
                onButtonPressed = onButtonPressed,
                onIndexChanged = { selectedIndex = it }
            ) {
                nameValid = name.isNotEmpty() && name.length <= 30
                emailValid = isValidEmail(email)
                passwordValid = pass.isNotEmpty() && pass.length <= 30
                confirmValid = pass2 == pass
                indexValid = selectedIndex != 0
                formValid = nameValid && emailValid && passwordValid && confirmValid && indexValid
            }
            AnimatedVisibility(visible = isLoading) {
                LinearProgressIndicator(Modifier.width(300.dp))
            }
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun RegisterForm(
    items: List<String>,
    name: String,
    onNameUpdate: (String) -> Unit,
    nameValid: Boolean,
    email: String,
    onEmailUpdated: (String) -> Unit,
    emailValid: Boolean,
    password: String,
    onPasswordUpdated: (String) -> Unit,
    passwordValid: Boolean,
    confirm: String,
    onConfirmUpdated: (String) -> Unit,
    confirmValid: Boolean,
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    indexValid: Boolean,
    formValid: Boolean,
    loading: Boolean,
    onButtonPressed: () -> Unit,
    validate: () -> Unit
) {
    val (nField, eField, p1Field, p2Field) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var visible by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column {
            Text(text = "Registrate", style = MaterialTheme.typography.h3)
            Spacer(Modifier.height(8.dp))
            Card {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(nField)
                            .width(280.dp),
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onNext = { eField.requestFocus() }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        isError = !nameValid,
                        value = name,
                        onValueChange =
                        {
                            onNameUpdate(it)
                            validate()
                        },
                        label = { Text("Nombre") } )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(eField)
                            .width(280.dp),
                        keyboardActions = KeyboardActions(
                            onNext = { p1Field.requestFocus() }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        singleLine = true,
                        isError = !emailValid,
                        value = email, onValueChange =
                        {
                            onEmailUpdated(it)
                            validate()
                        },
                        label = { Text("Correo") } )
                    Spacer(Modifier.height(8.dp))
                    Dropdown(
                        items = items,
                        onItemSelected =
                        {
                            onIndexChanged(it)
                            validate()
                        },
                        index = selectedIndex
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(isError = !passwordValid, value = password,
                        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(painter = painterResource(
                                id = if (visible) R.drawable.ic_baseline_visibility_24
                                else R.drawable.ic_baseline_visibility_off_24),
                                contentDescription = if (visible) "Ofuscar contraseña." else "Mostrar contraseña.",
                            modifier = Modifier.clickable { visible = !visible })
                        },
                        modifier = Modifier
                            .focusRequester(p1Field)
                            .width(280.dp),
                        keyboardActions = KeyboardActions(
                            onNext = { p2Field.requestFocus() }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Password
                        ),
                        singleLine = true,
                        onValueChange =
                        {
                            onPasswordUpdated(it)
                            validate()
                        }, label = { Text("Contraseña") } )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(isError = !confirmValid,
                        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .focusRequester(p2Field)
                            .width(280.dp),
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                validate()
                                onButtonPressed()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        value = confirm, onValueChange =
                        {
                            onConfirmUpdated(it)
                            validate()
                        },
                        label = { Text("Confirmar contraseña") } )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        enabled = !loading,
                        onClick =
                        {
                            validate()
                            onButtonPressed()
                        },
                        modifier = Modifier.width(280.dp)) {
                        Text(text = if (loading) "Registrando..." else "Registrarse")
                    }
                }
            }
        }
    }
}
