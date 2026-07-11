package com.ghosttalk.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.domain.model.SavedAccount
import com.ghosttalk.presentation.auth.AuthFlowViewModel
import com.ghosttalk.presentation.auth.RegisterStep
import com.ghosttalk.ui.components.GhostLogo
import com.ghosttalk.ui.components.GhostOutlinedButton
import com.ghosttalk.ui.components.GhostPrimaryButton
import com.ghosttalk.ui.components.LoadingScreen
import com.ghosttalk.ui.components.auth.AccountCard
import com.ghosttalk.ui.theme.GhostSpacing

@Composable
fun AccountHubScreen(
    onAuthSuccess: () -> Unit,
    onStartRegister: () -> Unit,
    viewModel: AuthFlowViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var accountToRemove by remember { mutableStateOf<SavedAccount?>(null) }

    LaunchedEffect(state.success) { if (state.success) onAuthSuccess() }
    LaunchedEffect(state.error) {
        state.error?.let { snackbar.showSnackbar(it); viewModel.clearError() }
    }

    if (state.selectedAccount != null) {
        PinUnlockScreen(
            account = state.selectedAccount!!,
            pinLength = AuthFlowViewModel.PIN_LENGTH,
            filledCount = state.pinInput.length,
            isLoading = state.isLoading,
            onDigit = viewModel::onPinDigit,
            onDelete = viewModel::onPinDelete,
            onBack = viewModel::clearSelectedAccount
        )
        return
    }

    accountToRemove?.let { account ->
        AlertDialog(
            onDismissRequest = { accountToRemove = null },
            title = { Text(stringResource(R.string.remove_account)) },
            text = { Text(stringResource(R.string.remove_account_confirm, account.displayName)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.removeAccount(account)
                    accountToRemove = null
                }) { Text(stringResource(R.string.remove)) }
            },
            dismissButton = {
                TextButton(onClick = { accountToRemove = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (state.isLoading && state.accounts.isEmpty()) {
            LoadingScreen(Modifier.padding(padding))
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .safeDrawingPadding()
                .padding(GhostSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GhostLogo(size = 72.dp)
            Spacer(Modifier.height(GhostSpacing.md))
            Text(
                text = stringResource(R.string.select_account),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.accounts_slots_info,
                    state.maxAccounts - state.remainingSlots, state.maxAccounts),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(GhostSpacing.lg))
            if (state.accounts.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_accounts_yet),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(GhostSpacing.lg))
                GhostPrimaryButton(
                    text = stringResource(R.string.create_identity),
                    onClick = onStartRegister
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(GhostSpacing.sm)
                ) {
                    items(state.accounts, key = { it.userId }) { account ->
                        AccountCard(
                            account = account,
                            onClick = { viewModel.selectAccount(account) },
                            onRemove = { accountToRemove = account }
                        )
                    }
                }
                Spacer(Modifier.height(GhostSpacing.md))
                if (state.remainingSlots > 0) {
                    GhostPrimaryButton(
                        text = stringResource(R.string.add_account),
                        onClick = onStartRegister
                    )
                }
                GhostOutlinedButton(
                    text = stringResource(R.string.use_another_account),
                    onClick = onStartRegister,
                    modifier = Modifier.padding(top = GhostSpacing.sm)
                )
            }
        }
    }
}
