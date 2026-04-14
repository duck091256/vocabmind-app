package com.example.voicemind.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.voicemind.ui.screens.auth.LoginScreen
import com.example.voicemind.ui.screens.auth.RegisterScreen
import com.example.voicemind.ui.screens.home.HomeDashboard
import com.example.voicemind.ui.screens.onboarding.OnboardingScreen
import com.example.voicemind.ui.viewmodel.OnboardingViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val startDestination by onboardingViewModel.startDestination.collectAsStateWithLifecycle()

    if (startDestination == null) {
        // Đang load — hiện splash đơn giản
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate("onboarding") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("onboarding") {
            OnboardingScreen(
                onFinished = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeDashboard(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}