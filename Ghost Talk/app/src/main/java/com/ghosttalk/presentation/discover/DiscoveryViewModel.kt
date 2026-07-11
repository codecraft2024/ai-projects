package com.ghosttalk.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.core.network.NetworkErrorMapper
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.usecase.CreateChatUseCase
import com.ghosttalk.domain.usecase.DiscoverUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val discoverUsersUseCase: DiscoverUsersUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<List<GhostUser>>(emptyList())
    val users: StateFlow<List<GhostUser>> = _users.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _chatCreated = MutableStateFlow<Chat?>(null)
    val chatCreated: StateFlow<Chat?> = _chatCreated.asStateFlow()

    private var searchJob: Job? = null

    init {
        onQueryChange("")
    }

    fun onQueryChange(value: String) {
        _query.value = value
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            search(value)
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            discoverUsersUseCase(query)
                .catch { e ->
                    _error.value = NetworkErrorMapper.toUserMessage(e)
                    _isLoading.value = false
                    emit(emptyList())
                }
                .collect {
                    _users.value = it
                    _isLoading.value = false
                }
        }
    }

    fun startChat(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            createChatUseCase(userId)
                .onSuccess { _chatCreated.value = it }
                .onFailure {
                    _error.value = NetworkErrorMapper.toUserMessage(it)
                    _isLoading.value = false
                }
        }
    }

    fun clearChatCreated() {
        _chatCreated.value = null
        _isLoading.value = false
    }

    fun clearError() {
        _error.value = null
    }
}
