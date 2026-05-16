package com.example.voicemind.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.example.voicemind.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.voicemind.ui.components.SpeedDialFab
import com.example.voicemind.ui.screens.auth.LoginScreen
import com.example.voicemind.ui.screens.auth.RegisterScreen
import com.example.voicemind.ui.screens.game.WordChainScreen
import com.example.voicemind.ui.screens.home.HomeDashboard
import com.example.voicemind.ui.screens.lessons.LessonsScreen
import com.example.voicemind.ui.screens.lessons.StudyScreen
import com.example.voicemind.ui.screens.lessons.StudyViewModel
import com.example.voicemind.ui.screens.onboarding.OnboardingScreen
import com.example.voicemind.ui.screens.onboarding.OnboardingViewModel
import com.example.voicemind.ui.screens.profile.ProfileScreen
import com.example.voicemind.ui.screens.review.ReviewScreen
import com.example.voicemind.ui.screens.sets.CreateSetScreen
import com.example.voicemind.ui.screens.sets.ExploreSetsScreen
import com.example.voicemind.ui.screens.sets.FriendsPacksScreen
import com.example.voicemind.ui.screens.sets.MySetsScreen
import com.example.voicemind.ui.screens.sets.SetDetailScreen
import com.example.voicemind.ui.screens.settings.SettingsScreen
import com.example.voicemind.ui.screens.spaced_repetition.SpacedRepetitionInfoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationRoot(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteString = currentBackStackEntry?.destination?.route
    val selectedNavRoute = NavRoute.entries.find { it.route == currentRouteString }
    var isFabMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currentRouteString) {
        isFabMenuExpanded = false
    }

    Scaffold(
        topBar = {
            if (selectedNavRoute == NavRoute.SETS) {
//                MySetsTopBar()
            } else if (currentRouteString == NavRoute.EXPLORE_SETS || currentRouteString == NavRoute.FRIEND_PACK) {
                val title = if (currentRouteString == NavRoute.EXPLORE_SETS) "Explore Sets" else "VocabMind"
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF4B5563)
                            )
                        }
                    },
                    actions = {
                        Image(
                            painter = painterResource(id = R.drawable.man),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD591)),
                            contentScale = ContentScale.Crop
                        )
                    },
                    windowInsets = WindowInsets(0),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        },
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
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedNavRoute == NavRoute.SETS,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                SpeedDialFab(
                    isExpanded = isFabMenuExpanded,
                    onToggle = { isFabMenuExpanded = !isFabMenuExpanded },
                    onCreateNew = {
                        isFabMenuExpanded = false
                        navController.navigate(NavRoute.CREATE_SET)
                    },
                    onDownload = {
                        isFabMenuExpanded = false
                        navController.navigate(NavRoute.EXPLORE_SETS)
                    },
                    onAddFromFriends = {
                        isFabMenuExpanded = false
                        navController.navigate(NavRoute.FRIEND_PACK)
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
                    navController = navController,
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo(NavRoute.HOME.route) { inclusive = true }
                        }
                    },
                    onNavigateToReview = { navController.navigate("review") }   // 👈 Thêm dòng này
                )
            }

            composable(NavRoute.SETS.route) {
                MySetsScreen(
                    onNavigateToInfoSpacedRepetition = { navController.navigate("SPinfo") },
                    onNavigateToDetail = { setId ->
                        navController.navigate("${NavRoute.SET_DETAIL}/$setId")
                    },
                    onNavigateBack = { navController.popBackStack() }

                )
            }

            composable("SPinfo") {
                SpacedRepetitionInfoScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable("${NavRoute.SET_DETAIL}/{setId}") { backStackEntry ->
                val setId = backStackEntry.arguments?.getString("setId") ?: return@composable
                SetDetailScreen(
                    setId = setId,
                    onNavigateBack = { navController.popBackStack() },
                    onAddWordsClick = { navController.navigate("${NavRoute.CREATE_SET}?setId=$setId") }
                )
            }

            composable(
                route = "${NavRoute.CREATE_SET}?setId={setId}",
                arguments = listOf(navArgument("setId") { 
                    type = NavType.StringType
                    nullable = true 
                })
            ) { backStackEntry ->
                CreateSetScreen(
                    onSaveSuccess = {
                        navController.popBackStack()
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(NavRoute.EXPLORE_SETS) {
                ExploreSetsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(NavRoute.FRIEND_PACK) {
                FriendsPacksScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(NavRoute.PROFILE.route) {
                ProfileScreen(
                    onSignOut = {
                        navController.navigate("login") {
                            popUpTo(NavRoute.HOME.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(NavRoute.SETTINGS.route) {
                SettingsScreen()
            }

            // Lessons screen
            composable("lessons") {
                LessonsScreen(
                    onLessonClick = { lesson ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("lessonId", lesson.id)
                        navController.navigate("study")
                    },
                    onBackToDashboard = { navController.navigate(NavRoute.HOME.route) {
                        popUpTo("lessons") { inclusive = true }
                    } }
                )
            }

            // Study screen
            composable("study") {
                val lessonId = navController.previousBackStackEntry?.savedStateHandle?.get<String>("lessonId")
                if (lessonId != null) {
                    val studyViewModel: StudyViewModel = hiltViewModel()
                    LaunchedEffect(lessonId) {
                        studyViewModel.loadLesson(lessonId)
                    }
                    val lesson by studyViewModel.lesson.collectAsStateWithLifecycle()
                    val isLoading by studyViewModel.isLoading.collectAsStateWithLifecycle()

                    when {
                        isLoading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        lesson != null -> {
                            StudyScreen(lesson = lesson!!, onBack = { navController.popBackStack() })
                        }
                        else -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Không tìm thấy bài học")
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Lỗi: thiếu ID bài học")
                    }
                }
            }

            composable("review") {
                ReviewScreen(onBack = { navController.popBackStack() })
            }

            composable(NavRoute.WORD_CHAIN_GAME) {
                WordChainScreen(onBack = { navController.popBackStack() })
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

            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySetsTopBar() {
    Surface(
        shadowElevation = 2.dp,
        color = Color.White
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD591)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.man),
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }

                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            actions = {
                Icon(
                    painter = painterResource(id = R.drawable.sparkle),
                    contentDescription = null,
                    tint = Color(0xFF6200EE),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                )
            },
            windowInsets = WindowInsets(0),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
    }
}
