package com.ghosttalk.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.usecase.LoginWithMobileUseCase
import com.ghosttalk.domain.usecase.LoginWithNicknameUseCase
import com.ghosttalk.domain.usecase.LogoutUseCase
import com.ghosttalk.domain.usecase.SendOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val otpSent: Boolean = false,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val loginWithMobileUseCase: LoginWithMobileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun sendOtp(phoneNumber: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            sendOtpUseCase(phoneNumber)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, otpSent = true)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Failed to send OTP"
                    )
                }
        }
    }

    fun loginWithMobile(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = loginWithMobileUseCase(phoneNumber, otp)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                AuthResult.Loading -> Unit
            }
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val loginWithNicknameUseCase: LoginWithNicknameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _selectedAvatar = MutableStateFlow("ghost_1")
    val selectedAvatar: StateFlow<String> = _selectedAvatar.asStateFlow()

    fun selectAvatar(avatarId: String) {
        _selectedAvatar.value = avatarId
    }

    fun loginWithNickname(nickname: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = loginWithNicknameUseCase(nickname, _selectedAvatar.value)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                AuthResult.Loading -> Unit
            }
        }
    }
}

@HiltViewModel
class MobileLoginViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val loginWithMobileUseCase: LoginWithMobileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun sendOtp(phoneNumber: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            sendOtpUseCase(phoneNumber)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, otpSent = true)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message
                    )
                }
        }
    }

    fun verifyOtp(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = loginWithMobileUseCase(phoneNumber, otp)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                AuthResult.Loading -> Unit
            }
        }
    }
}
