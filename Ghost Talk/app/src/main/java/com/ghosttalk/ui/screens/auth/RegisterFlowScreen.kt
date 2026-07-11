package com.ghosttalk.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.core.auth.SecureScreenEffect
import com.ghosttalk.presentation.auth.AuthFlowViewModel
import com.ghosttalk.presentation.auth.RegisterStep
import com.ghosttalk.ui.components.GhostOutlinedButton
import com.ghosttalk.ui.components.GhostPrimaryButton
import com.ghosttalk.ui.components.GhostTopBar
import com.ghosttalk.ui.components.LoadingScreen
import com.ghosttalk.ui.components.auth.AvatarPickerGrid
import com.ghosttalk.ui.components.auth.PinEntrySection
import com.ghosttalk.ui.theme.GhostSpacing

@Composable
fun RegisterFlowScreen(
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthFlowViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.success) { if (state.success) onSuccess() }
    LaunchedEffect(state.error) {
        state.error?.let { snackbar.showSnackbar(it); viewModel.clearError() }
    }

    if (state.registerStep == RegisterStep.CREATE_PIN ||
        state.registerStep == RegisterStep.CONFIRM_PIN
    ) {
        SecureScreenEffect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            GhostTopBar(
                title = stringResource(R.string.register_title),
                onBackClick = {
                    if (state.registerStep == RegisterStep.METHOD) onBack()
                    else viewModel.cancelRegister()
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (state.isLoading) {
            LoadingScreen(Modifier.padding(padding))
            return@Scaffold
        }
        when (state.registerStep) {
            RegisterStep.CREATE_PIN, RegisterStep.CONFIRM_PIN -> {
                val filled = if (state.registerStep == RegisterStep.CREATE_PIN) {
                    state.pinDraft.length
                } else {
                    state.pinInput.length
                }
                PinEntrySection(
                    title = if (state.registerStep == RegisterStep.CREATE_PIN) {
                        stringResource(R.string.create_pin)
                    } else {
                        stringResource(R.string.confirm_pin)
                    },
                    subtitle = if (state.registerStep == RegisterStep.CREATE_PIN) {
                        stringResource(R.string.create_pin_subtitle)
                    } else {
                        stringResource(R.string.confirm_pin_subtitle)
                    },
                    pinLength = AuthFlowViewModel.PIN_LENGTH,
                    filledCount = filled,
                    onDigit = viewModel::onRegisterPinDigit,
                    onDelete = viewModel::onRegisterPinDelete,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .safeDrawingPadding()
                        .padding(horizontal = GhostSpacing.lg)
                )
            }
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(GhostSpacing.lg)
            ) {
                when (state.registerStep) {
                RegisterStep.METHOD -> RegisterMethodStep(
                    onMobile = { viewModel.setRegisterMethod(true) },
                    onUsername = { viewModel.setRegisterMethod(false) }
                )
                RegisterStep.DETAILS -> RegisterDetailsStep(
                    state = state,
                    onUsernameChange = viewModel::updateUsername,
                    onMobileChange = viewModel::updateMobile,
                    onDisplayNameChange = viewModel::updateDisplayName,
                    onCheckUsername = viewModel::checkUsernameAvailability,
                    onContinue = viewModel::proceedFromDetails
                )
                RegisterStep.AVATAR -> {
                    Text(
                        text = stringResource(R.string.select_avatar),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(GhostSpacing.md))
                    AvatarPickerGrid(
                        selectedId = state.selectedAvatarId,
                        onSelected = viewModel::selectAvatar
                    )
                    Spacer(Modifier.height(GhostSpacing.xl))
                    GhostPrimaryButton(
                        text = stringResource(R.string.continue_btn),
                        onClick = viewModel::completeRegistration
                    )
                }
                else -> Unit
                }
            }
        }
    }
}

@Composable
private fun RegisterMethodStep(onMobile: () -> Unit, onUsername: () -> Unit) {
    Text(
        text = stringResource(R.string.register_method_title),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(GhostSpacing.sm))
    Text(
        text = stringResource(R.string.register_method_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(GhostSpacing.xl))
    GhostPrimaryButton(text = stringResource(R.string.register_with_mobile), onClick = onMobile)
    Spacer(Modifier.height(GhostSpacing.sm))
    GhostOutlinedButton(text = stringResource(R.string.register_with_username), onClick = onUsername)
}

@Composable
private fun RegisterDetailsStep(
    state: com.ghosttalk.presentation.auth.AuthFlowUiState,
    onUsernameChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onCheckUsername: () -> Unit,
    onContinue: () -> Unit
) {
    OutlinedTextField(
        value = state.username,
        onValueChange = onUsernameChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.nickname_hint)) },
        singleLine = true,
        isError = state.usernameAvailable == false
    )
    GhostOutlinedButton(
        text = stringResource(R.string.check_username),
        onClick = onCheckUsername,
        modifier = Modifier.padding(vertical = GhostSpacing.sm),
        enabled = !state.usernameChecking
    )
    if (state.usernameAvailable == true) {
        Text(
            text = stringResource(R.string.username_available),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall
        )
    }
    if (state.registerWithMobile) {
        Spacer(Modifier.height(GhostSpacing.sm))
        OutlinedTextField(
            value = state.mobile,
            onValueChange = onMobileChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.phone_hint)) },
            singleLine = true
        )
    }
    Spacer(Modifier.height(GhostSpacing.sm))
    OutlinedTextField(
        value = state.displayName,
        onValueChange = onDisplayNameChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.display_name_hint)) },
        singleLine = true
    )
    Spacer(Modifier.height(GhostSpacing.xl))
    GhostPrimaryButton(text = stringResource(R.string.next), onClick = onContinue)
}
