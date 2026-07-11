package com.ghosttalk.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.OnlineStatus
import com.ghosttalk.domain.model.TypingState
import android.net.Uri
import com.ghosttalk.domain.usecase.GetChatsUseCase
import com.ghosttalk.domain.usecase.GetCurrentUserUseCase
import com.ghosttalk.domain.usecase.GetMessagesUseCase
import com.ghosttalk.domain.usecase.MarkChatAsReadUseCase
import com.ghosttalk.domain.usecase.ObserveOnlineStatusUseCase
import com.ghosttalk.domain.usecase.ObserveTypingUseCase
import com.ghosttalk.domain.usecase.SendMessageUseCase
import com.ghosttalk.core.network.NetworkErrorMapper
import com.ghosttalk.domain.usecase.SetTypingUseCase
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
class ChatListViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentUserId = MutableStateFlow("")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _currentUserId.value = user?.ghostId ?: ""
            }
        }
        viewModelScope.launch {
            getChatsUseCase().collect {
                _chats.value = it
                _isLoading.value = false
            }
        }
    }
}

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markChatAsReadUseCase: MarkChatAsReadUseCase,
    private val setTypingUseCase: SetTypingUseCase,
    private val observeTypingUseCase: ObserveTypingUseCase,
    private val observeOnlineStatusUseCase: ObserveOnlineStatusUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val chatId: String = savedStateHandle.get<String>("chatId") ?: ""
    val participantName: String = savedStateHandle.get<String>("participantName")
        ?.let { Uri.decode(it) } ?: ""
    val participantId: String = savedStateHandle.get<String>("participantId") ?: ""

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _currentUserId = MutableStateFlow("")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _currentUserNickname = MutableStateFlow("")
    val currentUserNickname: StateFlow<String> = _currentUserNickname.asStateFlow()

    private val _typingState = MutableStateFlow<TypingState?>(null)
    val typingState: StateFlow<TypingState?> = _typingState.asStateFlow()

    private val _onlineStatus = MutableStateFlow<OnlineStatus?>(null)
    val onlineStatus: StateFlow<OnlineStatus?> = _onlineStatus.asStateFlow()

    private val _sendError = MutableStateFlow<String?>(null)
    val sendError: StateFlow<String?> = _sendError.asStateFlow()

    private var typingJob: Job? = null

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _currentUserId.value = user?.ghostId ?: ""
                _currentUserNickname.value = user?.nickname ?: ""
            }
        }
        viewModelScope.launch {
            getMessagesUseCase(chatId).collect { _messages.value = it }
        }
        viewModelScope.launch {
            observeTypingUseCase(chatId).collect { _typingState.value = it }
        }
        if (participantId.isNotBlank()) {
            viewModelScope.launch {
                observeOnlineStatusUseCase(participantId).collect {
                    _onlineStatus.value = it
                }
            }
        }
        viewModelScope.launch { markChatAsReadUseCase(chatId) }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            sendMessageUseCase(chatId, content.trim())
                .onFailure { _sendError.value = NetworkErrorMapper.toUserMessage(it) }
        }
    }

    fun clearSendError() {
        _sendError.value = null
    }

    fun onTypingChanged(isTyping: Boolean) {
        typingJob?.cancel()
        if (isTyping) {
            viewModelScope.launch { setTypingUseCase(chatId, true) }
            typingJob = viewModelScope.launch {
                delay(3000)
                setTypingUseCase(chatId, false)
            }
        } else {
            viewModelScope.launch { setTypingUseCase(chatId, false) }
        }
    }
}

