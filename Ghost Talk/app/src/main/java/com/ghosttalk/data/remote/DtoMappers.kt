package com.ghosttalk.data.remote

import com.ghosttalk.data.remote.dto.UserDto
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.MessageStatus
import com.ghosttalk.data.remote.dto.ChatDto
import com.ghosttalk.data.remote.dto.MessageDto

fun UserDto.toDomain(): GhostUser = GhostUser(
    ghostId = ghostId,
    nickname = nickname,
    avatarResId = avatarId,
    isOnline = isOnline,
    lastSeen = lastSeen
)

fun MessageDto.toDomain(): Message = Message(
    id = id,
    chatId = chatId,
    senderId = senderId,
    content = content,
    timestamp = timestamp,
    status = MessageStatus.valueOf(status),
    isEncrypted = isEncrypted
)

fun ChatDto.toDomain(): com.ghosttalk.domain.model.Chat =
    com.ghosttalk.domain.model.Chat(
        id = id,
        participant = participant.toDomain(),
        lastMessage = lastMessage?.toDomain(),
        unreadCount = unreadCount,
        updatedAt = updatedAt
    )
