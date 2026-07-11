package com.ghosttalk.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.core.auth.AccountManager
import com.ghosttalk.core.auth.IdentifierUtils
import com.ghosttalk.core.utils.NicknameValidator
import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.SavedAccount
import com.ghosttalk.domain.usecase.CheckUsernameUseCase
import com.ghosttalk.domain.usecase.ClearActiveSessionUseCase
import com.ghosttalk.domain.usecase.GetDeviceAccountsUseCase
import com.ghosttalk.domain.usecase.LoginWithPinUseCase
import com.ghosttalk.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class RegisterStep { METHOD, DETAILS, CREATE_PIN, CONFIRM_PIN, AVATAR }

data class AuthFlowUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val accounts: List<SavedAccount> = emptyList(),
    val remainingSlots: Int = 3,
    val maxAccounts: Int = 3,
    val selectedAccount: SavedAccount? = null,
    val registerStep: RegisterStep = RegisterStep.METHOD,
    val registerWithMobile: Boolean = false,
    val username: String = "",
    val mobile: String = "",
    val displayName: String = "",
    val usernameAvailable: Boolean? = null,
    val usernameChecking: Boolean = false,
    val pinDraft: String = "",
    val selectedAvatarId: String = "modern_01",
    val pinInput: String = ""
)

@HiltViewModel
class AuthFlowViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginWithPinUseCase: LoginWithPinUseCase,
    private val getDeviceAccountsUseCase: GetDeviceAccountsUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase,
    private val clearActiveSessionUseCase: ClearActiveSessionUseCase,
    private val accountManager: AccountManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthFlowUiState())
    val uiState: StateFlow<AuthFlowUiState> = _uiState.asStateFlow()

    init {
        refreshAccounts()
    }

    fun refreshAccounts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val local = accountManager.accounts.value
            getDeviceAccountsUseCase()
                .onSuccess { info ->
                    val merged = mergeAccounts(local, info.accounts)
                    info.accounts.forEach { accountManager.upsertAccount(it) }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        accounts = merged,
                        remainingSlots = info.remainingSlots,
                        maxAccounts = info.maxAccounts
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        accounts = local,
                        error = if (local.isEmpty()) it.message else null
                    )
                }
        }
    }

    fun selectAccount(account: SavedAccount) {
        viewModelScope.launch {
            clearActiveSessionUseCase()
            _uiState.value = _uiState.value.copy(
                selectedAccount = account,
                pinInput = "",
                error = null
            )
        }
    }

    fun clearSelectedAccount() {
        _uiState.value = _uiState.value.copy(selectedAccount = null, pinInput = "", error = null)
    }

    fun onPinDigit(digit: Int) {
        val current = _uiState.value.pinInput
        if (current.length >= PIN_LENGTH) return
        val updated = current + digit
        _uiState.value = _uiState.value.copy(pinInput = updated, error = null)
        if (updated.length == PIN_LENGTH) {
            submitPin(updated)
        }
    }

    fun onPinDelete() {
        val current = _uiState.value.pinInput
        if (current.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(pinInput = current.dropLast(1), error = null)
        }
    }

    private fun submitPin(pin: String) {
        val account = _uiState.value.selectedAccount ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = loginWithPinUseCase(account.username, pin)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthFlowUiState(success = true)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        pinInput = "",
                        error = result.message
                    )
                }
                AuthResult.Loading -> Unit
            }
        }
    }

    fun startRegister() {
        if (_uiState.value.remainingSlots <= 0) {
            _uiState.value = _uiState.value.copy(
                error = "Maximum of ${_uiState.value.maxAccounts} accounts reached on this device."
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            registerStep = RegisterStep.METHOD,
            registerWithMobile = false,
            username = "",
            mobile = "",
            displayName = "",
            pinDraft = "",
            pinInput = "",
            selectedAvatarId = "modern_01",
            error = null
        )
    }

    fun setRegisterMethod(useMobile: Boolean) {
        _uiState.value = _uiState.value.copy(
            registerStep = RegisterStep.DETAILS,
            registerWithMobile = useMobile
        )
    }

    fun updateUsername(value: String) {
        _uiState.value = _uiState.value.copy(username = value, usernameAvailable = null)
    }

    fun updateMobile(value: String) {
        _uiState.value = _uiState.value.copy(mobile = value)
    }

    fun updateDisplayName(value: String) {
        _uiState.value = _uiState.value.copy(displayName = value)
    }

    fun checkUsernameAvailability() {
        val username = _uiState.value.username.trim()
        val validationError = NicknameValidator.getError(username)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(error = validationError)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(usernameChecking = true, error = null)
            checkUsernameUseCase(username)
                .onSuccess { available ->
                    _uiState.value = _uiState.value.copy(
                        usernameChecking = false,
                        usernameAvailable = available,
                        error = if (!available) "Username already taken" else null
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        usernameChecking = false,
                        error = it.message
                    )
                }
        }
    }

    fun proceedFromDetails() {
        val state = _uiState.value
        val usernameError = NicknameValidator.getError(state.username.trim())
        if (usernameError != null) {
            _uiState.value = state.copy(error = usernameError)
            return
        }
        if (state.registerWithMobile && !IdentifierUtils.isValidPhone(state.mobile)) {
            _uiState.value = state.copy(error = "Enter a valid mobile number (10–15 digits)")
            return
        }
        if (state.usernameAvailable == false) {
            _uiState.value = state.copy(error = "Username already taken")
            return
        }
        _uiState.value = state.copy(registerStep = RegisterStep.CREATE_PIN, pinDraft = "", error = null)
    }

    fun onRegisterPinDigit(digit: Int) {
        val step = _uiState.value.registerStep
        val field = if (step == RegisterStep.CREATE_PIN) "pinDraft" else "pinInput"
        val current = if (step == RegisterStep.CREATE_PIN) _uiState.value.pinDraft else _uiState.value.pinInput
        if (current.length >= PIN_LENGTH) return
        val updated = current + digit
        if (step == RegisterStep.CREATE_PIN) {
            _uiState.value = _uiState.value.copy(pinDraft = updated, error = null)
            if (updated.length == PIN_LENGTH) {
                _uiState.value = _uiState.value.copy(registerStep = RegisterStep.CONFIRM_PIN, pinInput = "")
            }
        } else {
            _uiState.value = _uiState.value.copy(pinInput = updated, error = null)
            if (updated.length == PIN_LENGTH) {
                confirmRegisterPin(updated)
            }
        }
    }

    fun onRegisterPinDelete() {
        when (_uiState.value.registerStep) {
            RegisterStep.CREATE_PIN -> {
                val v = _uiState.value.pinDraft
                if (v.isNotEmpty()) _uiState.value = _uiState.value.copy(pinDraft = v.dropLast(1))
            }
            RegisterStep.CONFIRM_PIN -> {
                val v = _uiState.value.pinInput
                if (v.isNotEmpty()) _uiState.value = _uiState.value.copy(pinInput = v.dropLast(1))
            }
            else -> Unit
        }
    }

    private fun confirmRegisterPin(confirm: String) {
        val state = _uiState.value
        if (confirm != state.pinDraft) {
            _uiState.value = state.copy(
                error = "PINs do not match. Try again.",
                registerStep = RegisterStep.CREATE_PIN,
                pinDraft = "",
                pinInput = ""
            )
            return
        }
        _uiState.value = state.copy(registerStep = RegisterStep.AVATAR, error = null)
    }

    fun selectAvatar(avatarId: String) {
        _uiState.value = _uiState.value.copy(selectedAvatarId = avatarId)
    }

    fun completeRegistration() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            val mobile = if (state.registerWithMobile) {
                IdentifierUtils.normalizePhone(state.mobile)
            } else null
            when (val result = registerUseCase(
                username = state.username.trim(),
                pin = state.pinDraft,
                avatarId = state.selectedAvatarId,
                mobile = mobile,
                displayName = state.displayName.trim().ifBlank { state.username.trim() }
            )) {
                is AuthResult.Success -> _uiState.value = AuthFlowUiState(success = true)
                is AuthResult.Error -> _uiState.value = state.copy(isLoading = false, error = result.message)
                AuthResult.Loading -> Unit
            }
        }
    }

    fun removeAccount(account: SavedAccount) {
        accountManager.removeAccount(account.userId)
        refreshAccounts()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun cancelRegister() {
        _uiState.value = when (_uiState.value.registerStep) {
            RegisterStep.AVATAR -> _uiState.value.copy(
                registerStep = RegisterStep.CONFIRM_PIN,
                pinInput = "",
                error = null
            )
            RegisterStep.CONFIRM_PIN -> _uiState.value.copy(
                registerStep = RegisterStep.CREATE_PIN,
                pinDraft = "",
                pinInput = "",
                error = null
            )
            RegisterStep.CREATE_PIN -> _uiState.value.copy(
                registerStep = RegisterStep.DETAILS,
                pinDraft = "",
                error = null
            )
            RegisterStep.DETAILS -> _uiState.value.copy(
                registerStep = RegisterStep.METHOD,
                error = null
            )
            RegisterStep.METHOD -> _uiState.value
        }
    }

    private fun mergeAccounts(local: List<SavedAccount>, remote: List<SavedAccount>): List<SavedAccount> {
        val map = linkedMapOf<String, SavedAccount>()
        remote.forEach { map[it.userId] = it }
        local.forEach { existing ->
            map[existing.userId] = map[existing.userId]?.copy(lastActiveAt = existing.lastActiveAt) ?: existing
        }
        return map.values.sortedByDescending { it.lastActiveAt }
    }

    companion object {
        const val PIN_LENGTH = 6
    }
}
