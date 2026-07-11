package com.ghosttalk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.presentation.auth.AuthFlowViewModel
import com.ghosttalk.presentation.discover.UserProfileViewModel
import com.ghosttalk.presentation.splash.SplashDestination
import com.ghosttalk.ui.screens.auth.AccountHubScreen
import com.ghosttalk.ui.screens.auth.RegisterFlowScreen
import com.ghosttalk.ui.screens.chat.ChatDetailScreen
import com.ghosttalk.ui.screens.discover.UserProfileScreen
import com.ghosttalk.ui.screens.home.HomeScreen
import com.ghosttalk.ui.screens.onboarding.OnboardingScreen
import com.ghosttalk.ui.screens.splash.SplashScreen

@Composable
fun GhostTalkNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigate = { destination ->
                    val route = when (destination) {
                        SplashDestination.ONBOARDING -> Routes.ONBOARDING
                        SplashDestination.ACCOUNT_HUB -> Routes.ACCOUNT_HUB
                        SplashDestination.HOME -> Routes.HOME
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = { navController.navigate(Routes.ACCOUNT_HUB) }
            )
        }

        composable(Routes.ACCOUNT_HUB) { hubEntry ->
            val authViewModel: AuthFlowViewModel = hiltViewModel(hubEntry)
            AccountHubScreen(
                onAuthSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ACCOUNT_HUB) { inclusive = true }
                    }
                },
                onStartRegister = {
                    authViewModel.startRegister()
                    navController.navigate(Routes.REGISTER)
                },
                viewModel = authViewModel
            )
        }

        composable(Routes.REGISTER) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(Routes.ACCOUNT_HUB)
            }
            RegisterFlowScreen(
                viewModel = hiltViewModel(parentEntry),
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ACCOUNT_HUB) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onChatOpen = { chat -> navController.navigate(chat.toRoute()) },
                onUserOpen = { user ->
                    navController.navigate(Routes.userProfile(user.ghostId, user.nickname))
                },
                onLoggedOut = {
                    navController.navigate(Routes.ACCOUNT_HUB) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.CHAT_DETAIL,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("participantName") { type = NavType.StringType },
                navArgument("participantId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            ChatDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.USER_PROFILE,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { entry ->
            val profileViewModel: UserProfileViewModel = hiltViewModel(entry)
            UserProfileScreen(
                onBack = { navController.popBackStack() },
                onChatOpen = { chat -> navController.navigate(chat.toRoute()) },
                viewModel = profileViewModel
            )
        }
    }
}

private fun Chat.toRoute(): String =
    Routes.chatDetail(id, participant.nickname, participant.ghostId)
