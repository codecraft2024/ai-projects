package com.ghosttalk.ui.screens.discover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.presentation.discover.UserProfileViewModel
import com.ghosttalk.ui.components.GhostAvatar
import com.ghosttalk.ui.components.GhostOutlinedButton
import com.ghosttalk.ui.components.GhostPrimaryButton
import com.ghosttalk.ui.components.GhostTopBar
import com.ghosttalk.ui.components.LoadingScreen
import com.ghosttalk.ui.theme.GhostSpacing

@Composable
fun UserProfileScreen(
    onBack: () -> Unit,
    onChatOpen: (Chat) -> Unit,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isMessaging by viewModel.isMessaging.collectAsStateWithLifecycle()
    val chatCreated by viewModel.chatCreated.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(chatCreated) {
        chatCreated?.let {
            onChatOpen(it)
            viewModel.clearChatCreated()
        }
    }
    LaunchedEffect(error) {
        error?.let { snackbar.showSnackbar(it); viewModel.clearError() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { GhostTopBar(title = stringResource(R.string.profile_preview), onBackClick = onBack) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (isLoading || user == null) {
            LoadingScreen(Modifier.padding(padding))
            return@Scaffold
        }
        val profile = user!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .safeDrawingPadding()
                .padding(GhostSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GhostAvatar(avatarId = profile.avatarResId, size = 120.dp, isOnline = profile.isOnline)
            Spacer(Modifier.height(GhostSpacing.md))
            Text(profile.displayName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("@${profile.nickname}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (profile.verified) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            profile.bio?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(GhostSpacing.md))
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(Modifier.height(GhostSpacing.xl))
            GhostPrimaryButton(
                text = stringResource(R.string.message),
                onClick = viewModel::startChat,
                enabled = !isMessaging,
                modifier = Modifier.fillMaxWidth()
            )
            GhostOutlinedButton(
                text = stringResource(R.string.back),
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().padding(top = GhostSpacing.sm)
            )
        }
    }
}
