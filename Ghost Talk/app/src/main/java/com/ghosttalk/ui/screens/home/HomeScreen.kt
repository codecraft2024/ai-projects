package com.ghosttalk.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ghosttalk.R
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.ui.navigation.HomeTab
import com.ghosttalk.ui.screens.chat.ChatListScreen
import com.ghosttalk.ui.screens.discover.DiscoveryScreen
import com.ghosttalk.ui.screens.profile.ProfileScreen
import com.ghosttalk.ui.screens.settings.SettingsScreen

private data class BottomNavItem(
    val tab: HomeTab,
    val icon: ImageVector,
    val labelRes: Int
)

private val bottomNavItems = listOf(
    BottomNavItem(HomeTab.CHATS, Icons.AutoMirrored.Filled.Chat, R.string.chats),
    BottomNavItem(HomeTab.DISCOVER, Icons.Default.Explore, R.string.discover),
    BottomNavItem(HomeTab.PROFILE, Icons.Default.Person, R.string.profile),
    BottomNavItem(HomeTab.SETTINGS, Icons.Default.Settings, R.string.settings)
)

@Composable
fun HomeScreen(
    onChatOpen: (Chat) -> Unit,
    onUserOpen: (GhostUser) -> Unit,
    onLoggedOut: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                bottomNavItems.forEach { item ->
                    val label = stringResource(item.labelRes)
                    val selected = currentDestination?.hierarchy?.any { it.route == item.tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null
                            )
                        },
                        label = { Text(label) },
                        modifier = Modifier.semantics { contentDescription = label }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeTab.CHATS.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(HomeTab.CHATS.route) {
                ChatListScreen(onChatClick = onChatOpen)
            }
            composable(HomeTab.DISCOVER.route) {
                DiscoveryScreen(
                    onUserOpen = onUserOpen,
                    onChatOpen = onChatOpen
                )
            }
            composable(HomeTab.PROFILE.route) {
                ProfileScreen()
            }
            composable(HomeTab.SETTINGS.route) {
                SettingsScreen(onLoggedOut = onLoggedOut)
            }
        }
    }
}
