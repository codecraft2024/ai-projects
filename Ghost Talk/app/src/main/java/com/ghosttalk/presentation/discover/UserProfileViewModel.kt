package com.ghosttalk.presentation.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.core.network.NetworkErrorMapper
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.usecase.CreateChatUseCase
import com.ghosttalk.domain.usecase.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {

    private val userId: String = savedStateHandle.get<String>("userId") ?: ""

    private val _user = MutableStateFlow<GhostUser?>(null)
    val user: StateFlow<GhostUser?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isMessaging = MutableStateFlow(false)
    val isMessaging: StateFlow<Boolean> = _isMessaging.asStateFlow()

    private val _chatCreated = MutableStateFlow<Chat?>(null)
    val chatCreated: StateFlow<Chat?> = _chatCreated.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = getUserByIdUseCase(userId)
            _isLoading.value = false
        }
    }

    fun startChat() {
        val id = _user.value?.ghostId ?: return
        viewModelScope.launch {
            _isMessaging.value = true
            _error.value = null
            createChatUseCase(id)
                .onSuccess { _chatCreated.value = it }
                .onFailure {
                    _error.value = NetworkErrorMapper.toUserMessage(it)
                    _isMessaging.value = false
                }
        }
    }

    fun clearChatCreated() {
        _chatCreated.value = null
        _isMessaging.value = false
    }

    fun clearError() {
        _error.value = null
    }
}
