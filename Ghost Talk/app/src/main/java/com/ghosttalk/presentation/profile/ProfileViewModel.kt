package com.ghosttalk.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.core.network.NetworkErrorMapper
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.usecase.GetCurrentUserUseCase
import com.ghosttalk.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<GhostUser?>(null)
    val user: StateFlow<GhostUser?> = _user.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _showAvatarPicker = MutableStateFlow(false)
    val showAvatarPicker: StateFlow<Boolean> = _showAvatarPicker.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { _user.value = it }
        }
    }

    fun openAvatarPicker() {
        _showAvatarPicker.value = true
    }

    fun dismissAvatarPicker() {
        _showAvatarPicker.value = false
    }

    fun updateAvatar(avatarId: String) {
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null
            updateProfileUseCase(avatarId = avatarId)
                .onSuccess {
                    _user.value = it
                    _showAvatarPicker.value = false
                }
                .onFailure { _error.value = NetworkErrorMapper.toUserMessage(it) }
            _isSaving.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
