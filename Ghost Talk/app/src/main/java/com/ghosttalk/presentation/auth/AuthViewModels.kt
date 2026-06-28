package com.ghosttalk.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.usecase.LoginWithDeviceUseCase
import com.ghosttalk.domain.usecase.LogoutUseCase
import com.ghosttalk.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithDeviceUseCase: LoginWithDeviceUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun loginWithDevice() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = loginWithDeviceUseCase()) {
                is AuthResult.Success -> _uiState.value = AuthUiState(loginSuccess = true)
                is AuthResult.Error -> _uiState.value = AuthUiState(error = result.message)
                AuthResult.Loading -> Unit
            }
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _selectedAvatar = MutableStateFlow("ghost_1")
    val selectedAvatar: StateFlow<String> = _selectedAvatar.asStateFlow()

    fun selectAvatar(avatarId: String) {
        _selectedAvatar.value = avatarId
    }

    fun register(username: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = registerUseCase(username, _selectedAvatar.value)) {
                is AuthResult.Success -> _uiState.value = AuthUiState(loginSuccess = true)
                is AuthResult.Error -> _uiState.value = AuthUiState(error = result.message)
                AuthResult.Loading -> Unit
            }
        }
    }
}
