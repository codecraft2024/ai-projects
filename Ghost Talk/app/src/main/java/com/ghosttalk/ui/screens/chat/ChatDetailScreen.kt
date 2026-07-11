package com.ghosttalk.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.domain.model.Message
import com.ghosttalk.presentation.chat.ChatDetailViewModel
import com.ghosttalk.ui.components.ChatBubble
import com.ghosttalk.ui.components.GhostTopBar
import com.ghosttalk.ui.components.MessageInputBar
import com.ghosttalk.ui.theme.GhostSpacing

@Composable
fun ChatDetailScreen(
    onBack: () -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val currentUserId by viewModel.currentUserId.collectAsStateWithLifecycle()
    val currentUserNickname by viewModel.currentUserNickname.collectAsStateWithLifecycle()
    val typingState by viewModel.typingState.collectAsStateWithLifecycle()
    val onlineStatus by viewModel.onlineStatus.collectAsStateWithLifecycle()
    val sendError by viewModel.sendError.collectAsStateWithLifecycle()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(sendError) {
        sendError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSendError()
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    val statusText = when {
        typingState?.isTyping == true -> stringResource(R.string.typing)
        onlineStatus?.isOnline == true -> stringResource(R.string.online)
        onlineStatus != null -> stringResource(R.string.offline)
        else -> null
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            GhostTopBar(
                title = viewModel.participantName,
                subtitle = statusText,
                onBackClick = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            MessageInputBar(
                value = messageText,
                onValueChange = {
                    messageText = it
                    viewModel.onTypingChanged(it.isNotBlank())
                },
                onSend = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(messageText)
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(GhostSpacing.sm)
            )
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(messages, key = { it.id }) { message ->
                MessageRow(
                    message = message,
                    currentUserId = currentUserId,
                    currentUserNickname = currentUserNickname,
                    participantNickname = viewModel.participantName
                )
            }
        }
    }
}

@Composable
private fun MessageRow(
    message: Message,
    currentUserId: String,
    currentUserNickname: String,
    participantNickname: String
) {
    val isSent = message.senderId == currentUserId
    val senderName = when {
        isSent -> currentUserNickname
        message.senderUsername.isNotBlank() -> message.senderUsername
        else -> participantNickname
    }
    ChatBubble(
        content = message.content,
        senderName = senderName,
        timestamp = message.timestamp,
        isSent = isSent,
        status = message.status
    )
}
