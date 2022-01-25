package com.justdance.apptesis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.justdance.apptesis.screens.home.HomeScreen
import com.justdance.apptesis.screens.home.HomeViewModel
import com.justdance.apptesis.screens.login.LoginScreen
import com.justdance.apptesis.screens.login.LoginViewModel
import com.justdance.apptesis.screens.register.RegisterScreen
import com.justdance.apptesis.screens.register.RegisterViewModel
import com.justdance.apptesis.screens.start.StartScreen
import com.justdance.apptesis.screens.start.StartViewModel
import com.justdance.apptesis.ui.theme.AppTesisTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private val registerViewModel by viewModels<RegisterViewModel>()
    private val startViewModel by viewModels<StartViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var scope: CoroutineScope
    private lateinit var scaffoldState: ScaffoldState
    private lateinit var navHost: NavHostController

    @ExperimentalPermissionsApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TesisApp()
        }
    }

    @ExperimentalPermissionsApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @Composable
    fun TesisApp() {
        navHost = rememberNavController()
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()

        AppTesisTheme {
            Scaffold(
                scaffoldState = scaffoldState
            ) {
                NavHost(navController = navHost, startDestination = "identification") {
                    navigation(startDestination = "start", route = "identification") {
                        composable("start") {
                            StartScreen(navController = navHost, viewModel = startViewModel)
                        }
                        composable(
                            "login?redirected={redirected}",
                            arguments = listOf(navArgument("redirected") {defaultValue = false; type = NavType.BoolType})
                        ) { backStackEntry ->
                            backStackEntry.arguments?.let {
                                if (it.getBoolean("redirected")) {
                                    loginViewModel.ciChanged(registerViewModel.ciText.value!!)
                                    loginViewModel.passChanged(registerViewModel.passText.value!!)
                                }
                            }
                            LoginScreen(navController = navHost, viewModel = loginViewModel) {
                                    message, actionLabel, action ->
                                onSnack(message, actionLabel, action)
                            }
                        }
                        composable("register") {
                            RegisterScreen(navController = navHost, viewModel = registerViewModel) {
                                    message, actionLabel, action ->
                                onSnack(message, actionLabel, action)
                            }
                        }
                    }
                    navigation("home", "homeNav") {
                        composable("home") {
                            HomeScreen(navController = navHost, viewModel = homeViewModel)
                        }
                    }
                }
            }
        }
    }

    private fun onSnack(message: String, actionLabel: String?, action: SnackActions) {
        scope.launch {
            val result = scaffoldState.snackbarHostState.showSnackbar(message, actionLabel)
            if (result == SnackbarResult.Dismissed) return@launch
            when (action) {
                SnackActions.NONE -> {}
                SnackActions.REGISTER_GOTO_LOGIN -> {
                    navHost.navigate("login?redirected=true") {
                        popUpTo("login?redirected={redirected}") {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    fun TopBarRoute(route: String): Boolean {
        return when (route) {
            "login" -> false
            "register" -> false
            else -> true
        }
    }
}

enum class SnackActions {
    NONE,
    REGISTER_GOTO_LOGIN
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message)
{
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "A",
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var expanded by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (expanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )

        Column(modifier = Modifier.clickable { expanded = !expanded }) {
            Text(
                msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
                )
            Spacer(modifier = Modifier.height(4.dp))
            Surface( modifier = Modifier
                .animateContentSize()
                .padding(1.dp), shape = MaterialTheme.shapes.medium, elevation = 1.dp, color = surfaceColor) {
                Text(
                    text = msg.body,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) {
            item: Message ->
            MessageCard(msg = item)
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DefaultPreview() {
    AppTesisTheme {
        Conversation(messages = SampleData.conversationSample)
    }
}

object SampleData {
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            "Colleague",
            "Test...Test...Test..."
        ),
        Message(
            "Colleague",
            "List of Android versions:\n" +
                    "Android KitKat (API 19)\n" +
                    "Android Lollipop (API 21)\n" +
                    "Android Marshmallow (API 23)\n" +
                    "Android Nougat (API 24)\n" +
                    "Android Oreo (API 26)\n" +
                    "Android Pie (API 28)\n" +
                    "Android 10 (API 29)\n" +
                    "Android 11 (API 30)\n" +
                    "Android 12 (API 31)\n"
        ),
        Message(
            "Colleague",
            "I think Kotlin is my favorite programming language.\n" +
                    "It's so much fun!"
        ),
        Message(
            "Colleague",
            "Searching for alternatives to XML layouts..."
        ),
        Message(
            "Colleague",
            "Hey, take a look at Jetpack Compose, it's great!\n" +
                    "It's the Android's modern toolkit for building native UI." +
                    "It simplifies and accelerates UI development on Android." +
                    "Less code, powerful tools, and intuitive Kotlin APIs :)"
        ),
        Message(
            "Colleague",
            "It's available from API 21+ :)"
        ),
        Message(
            "Colleague",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(
            "Colleague",
            "Android Studio next version's name is Arctic Fox"
        ),
        Message(
            "Colleague",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(
            "Colleague",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(
            "Colleague",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(
            "Colleague",
            "Previews are also interactive after enabling the experimental setting"
        ),
        Message(
            "Colleague",
            "Have you tried writing build.gradle with KTS?"
        ),
    )
}