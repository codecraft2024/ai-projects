package com.ghosttalk.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.presentation.settings.SettingsViewModel
import com.ghosttalk.ui.components.GhostOutlinedButton
import com.ghosttalk.ui.theme.GhostSpacing
import com.ghosttalk.ui.theme.GhostTalkTheme

@Composable
fun SettingsScreen(
    onLoggedOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val loggedOut by viewModel.loggedOut.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (loggedOut) {
        androidx.compose.runtime.LaunchedEffect(Unit) { onLoggedOut() }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(stringResource(R.string.logout)) },
            text = { Text(stringResource(R.string.logout_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                }) {
                    Text(stringResource(R.string.logout))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(GhostSpacing.lg)
    ) {
        ListItem(
            headlineContent = { Text(stringResource(R.string.notifications)) },
            supportingContent = {
                Text(
                    stringResource(R.string.notifications),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text(stringResource(R.string.privacy)) },
            supportingContent = {
                Text(
                    "Anonymous by design",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text(stringResource(R.string.about)) },
            supportingContent = { Text(stringResource(R.string.version)) }
        )
        Spacer(modifier = Modifier.weight(1f))
        GhostOutlinedButton(
            text = stringResource(R.string.logout),
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    GhostTalkTheme { SettingsScreen(onLoggedOut = {}) }
}
