package com.example.voicemind.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.voicemind.ui.screens.auth.LoginScreen
import com.example.voicemind.ui.screens.auth.RegisterScreen
import com.example.voicemind.ui.screens.home.HomeDashboard
import com.example.voicemind.ui.screens.onboarding.OnboardingScreen
import com.example.voicemind.ui.screens.onboarding.OnboardingViewModel
import com.example.voicemind.ui.screens.profile.ProfileScreen
import com.example.voicemind.ui.screens.sets.StudySetsScreen
import com.example.voicemind.ui.screens.settings.SettingsScreen

@Composable
fun AppNavigationRoot(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteString = currentBackStackEntry?.destination?.route
    val selectedNavRoute = NavRoute.entries.find { it.route == currentRouteString }

    Scaffold(
        bottomBar = {
            if (selectedNavRoute != null) {
                AppBottomNavigationBar(
                    currentRoute = selectedNavRoute,
                    onNavigate = { route ->
                        navController.navigate(route.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "launcher",
            modifier = Modifier.padding(paddingValues)
        ) {

            // 🔥 Launcher (gate screen)
            composable("launcher") {
                val onboardingViewModel: OnboardingViewModel = hiltViewModel()
                val startDestination by onboardingViewModel.startDestination.collectAsStateWithLifecycle()

                if (startDestination == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    // 👉 điều hướng 1 lần
                    LaunchedEffect(startDestination) {
                        navController.navigate(startDestination!!) {
                            popUpTo("launcher") { inclusive = true }
                        }
                    }
                }
            }

            composable(NavRoute.HOME.route) {
                HomeDashboard(
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo(NavRoute.HOME.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavRoute.SETS.route) {
                StudySetsScreen()
            }

            composable(NavRoute.PROFILE.route) {
                ProfileScreen()
            }

            composable(NavRoute.SETTINGS.route) {
                SettingsScreen()
            }

            composable("login") {
                val onboardingViewModel: OnboardingViewModel = hiltViewModel()

                LoginScreen(
                    onLoginSuccess = {
                        onboardingViewModel.resolveDestinationAfterAuth()
                    },
                    onNavigateToRegister = { navController.navigate("register") }
                )

                val destination by onboardingViewModel.startDestination.collectAsStateWithLifecycle()
                LaunchedEffect(destination) {
                    destination?.let { dest ->
                        if (dest != "login") {
                            navController.navigate(dest) {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                }
            }

            composable("onboarding") {
                OnboardingScreen(
                    onFinished = {
                        navController.navigate(NavRoute.HOME.route) {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}