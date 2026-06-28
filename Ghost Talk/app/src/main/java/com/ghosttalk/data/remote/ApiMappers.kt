package com.ghosttalk.data.remote

import com.ghosttalk.data.remote.dto.ConversationSummaryDto
import com.ghosttalk.data.remote.dto.MessageDto
import com.ghosttalk.data.remote.dto.UserDto
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.MessageStatus
import java.time.Instant

fun UserDto.toGhostUser() = GhostUser(
    ghostId = id,
    nickname = username,
    avatarResId = avatarId,
    isOnline = online,
    lastSeen = lastSeen?.let { parseInstant(it) },
    accountCreatedAt = accountCreatedAt
)

fun ConversationSummaryDto.toDomain(currentUserId: String): Chat? {
    val participant = participant?.toGhostUser() ?: return null
    val lastMsg = lastMessage?.let {
        Message(
            id = it.id,
            chatId = id,
            senderId = it.senderId,
            content = it.content ?: "",
            timestamp = parseInstant(it.timestamp),
            status = MessageStatus.READ
        )
    }
    return Chat(
        id = id,
        participant = participant,
        lastMessage = lastMsg,
        unreadCount = unreadCount,
        updatedAt = parseInstant(updatedAt),
        isPinned = pinned,
        isMuted = muted,
        isArchived = archived,
        type = type
    )
}

fun MessageDto.toDomain(): Message = Message(
    id = id,
    chatId = conversationId,
    senderId = senderId,
    content = content ?: "",
    timestamp = parseInstant(timestamp),
    status = MessageStatus.SENT,
    replyToId = replyToId,
    isEdited = edited,
    isDeleted = deletedForAll,
    messageType = messageType
)

private fun parseInstant(value: String): Long = try {
    Instant.parse(value).toEpochMilli()
} catch (_: Exception) {
    System.currentTimeMillis()
}
