package com.ghosttalk.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.core.utils.DateTimeUtils
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.presentation.chat.ChatListViewModel
import com.ghosttalk.ui.components.EmptyState
import com.ghosttalk.ui.components.GhostAvatar
import com.ghosttalk.ui.components.LoadingScreen
import com.ghosttalk.ui.components.UnreadBadge
import com.ghosttalk.ui.theme.GhostSpacing
import com.ghosttalk.ui.theme.GhostTalkTheme

@Composable
fun ChatListScreen(
    onChatClick: (Chat) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val chats by viewModel.chats.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val currentUserId by viewModel.currentUserId.collectAsStateWithLifecycle()

    when {
        isLoading && chats.isEmpty() -> LoadingScreen()
        chats.isEmpty() -> EmptyState(stringResource(R.string.no_chats))
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(chats, key = { it.id }) { chat ->
                ChatListItem(
                    chat = chat,
                    currentUserId = currentUserId,
                    onClick = { onChatClick(chat) }
                )
            }
        }
    }
}

@Composable
private fun ChatListItem(
    chat: Chat,
    currentUserId: String,
    onClick: () -> Unit
) {
    val lastMessage = chat.lastMessage
    val preview = lastMessage?.let { msg ->
        val sender = when {
            msg.senderId == currentUserId -> stringResource(R.string.you)
            msg.senderUsername.isNotBlank() -> msg.senderUsername
            else -> chat.participant.nickname
        }
        "$sender: ${msg.content}"
    } ?: ""

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics { contentDescription = "Chat with ${chat.participant.nickname}" },
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GhostSpacing.md, vertical = GhostSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(GhostSpacing.sm)
        ) {
            GhostAvatar(
                avatarId = chat.participant.avatarResId,
                isOnline = chat.participant.isOnline,
                contentDescription = chat.participant.nickname
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.participant.nickname,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    lastMessage?.let {
                        Text(
                            text = DateTimeUtils.formatMessageTime(it.timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = preview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    UnreadBadge(count = chat.unreadCount)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatListPreview() {
    GhostTalkTheme { ChatListScreen(onChatClick = {}) }
}
