package com.heixss.findmypet.presenter.start

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.heixss.findmypet.presenter.bottomnav.BottomNavScreen
import com.heixss.findmypet.presenter.common.NavigationEvent
import com.heixss.findmypet.presenter.common.ObserveAsEvents
import com.heixss.findmypet.presenter.common.Screen
import com.heixss.findmypet.presenter.login.LoginScreen
import com.heixss.findmypet.presenter.login.LoginViewModel
import com.heixss.findmypet.presenter.register.RegisterScreen
import com.heixss.findmypet.presenter.register.RegisterViewModel

@Composable
fun StartScreen() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<StartScreenViewModel>()
    NavHost(
        navController = navController,
        startDestination = if (viewModel.isLoggedIn) Screen.BottomNav.route else Screen.Login.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Login.route) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                onLoginClicked = { username, password ->
                    loginViewModel.login(username, password)
                },
                isLoading = loginViewModel.isLoading,
                errorMsg = loginViewModel.loginErrorMsg,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
            ObserveAsEvents(flow = loginViewModel.actionChannelFlow) { event ->
                when (event) {
                    NavigationEvent.BottomNav -> {
                        navController.navigate(Screen.BottomNav.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            // Ensure the start destination is reselected if needed
                            launchSingleTop = true
                        }
                    }

                    else -> {}
                }
            }
        }
        composable(Screen.Register.route) {
            val registerViewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreen(
                isLoading = registerViewModel.isLoading,
                errorMsg = registerViewModel.registerErrorMsg
            ) { username, email, password, phone ->
                registerViewModel.register(
                    username = username,
                    password = password,
                    email = email,
                    phone = phone
                )
            }
            ObserveAsEvents(flow = registerViewModel.actionChannelFlow) { event ->
                when (event) {
                    NavigationEvent.BottomNav -> {
                        navController.navigate(Screen.BottomNav.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            // Ensure the start destination is reselected if needed
                            launchSingleTop = true
                        }
                    }

                    NavigationEvent.LogIn -> TODO()
                }
            }
        }
        composable(Screen.BottomNav.route) {
            BottomNavScreen {
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    // Ensure the start destination is reselected if needed
                    launchSingleTop = true
                }
            }
        }
    }
}