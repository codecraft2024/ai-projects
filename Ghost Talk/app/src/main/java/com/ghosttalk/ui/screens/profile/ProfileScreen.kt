package com.ghosttalk.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.presentation.profile.ProfileViewModel
import com.ghosttalk.ui.components.GhostAvatar
import com.ghosttalk.ui.components.GhostCard
import com.ghosttalk.ui.components.auth.AvatarPickerGrid
import com.ghosttalk.ui.theme.GhostSpacing
import com.ghosttalk.ui.theme.GhostTalkTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val showAvatarPicker by viewModel.showAvatarPicker.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let { snackbar.showSnackbar(it); viewModel.clearError() }
    }

    if (showAvatarPicker) {
        AlertDialog(
            onDismissRequest = viewModel::dismissAvatarPicker,
            title = { Text(stringResource(R.string.select_avatar)) },
            text = {
                AvatarPickerGrid(
                    selectedId = user?.avatarResId ?: "modern_01",
                    onSelected = viewModel::updateAvatar
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::dismissAvatarPicker) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(GhostSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(GhostSpacing.xl))
            Text(
                text = stringResource(R.string.your_ghost_identity),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(GhostSpacing.xl))
            user?.let {
                GhostCard {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GhostAvatar(
                            avatarId = it.avatarResId,
                            size = 100.dp,
                            contentDescription = it.nickname,
                            modifier = Modifier
                                .clickable(enabled = !isSaving, onClick = viewModel::openAvatarPicker)
                                .size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(GhostSpacing.sm))
                        Text(
                            text = stringResource(R.string.tap_to_change_avatar),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(GhostSpacing.md))
                        Text(
                            text = it.displayName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "@${it.nickname}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        it.bio?.takeIf { bio -> bio.isNotBlank() }?.let { bio ->
                            Spacer(modifier = Modifier.height(GhostSpacing.sm))
                            Text(
                                text = bio,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    GhostTalkTheme { ProfileScreen() }
}
