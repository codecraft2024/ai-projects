package com.ghosttalk.data.local

import com.ghosttalk.data.local.entity.ChatEntity
import com.ghosttalk.data.local.entity.MessageEntity
import com.ghosttalk.data.local.entity.UserEntity
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.MessageStatus

fun UserEntity.toDomain(): GhostUser = GhostUser(
    ghostId = ghostId,
    nickname = nickname,
    avatarResId = avatarResId,
    isOnline = isOnline,
    lastSeen = lastSeen
)

fun GhostUser.toEntity(): UserEntity = UserEntity(
    ghostId = ghostId,
    nickname = nickname,
    avatarResId = avatarResId,
    isOnline = isOnline,
    lastSeen = lastSeen
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    chatId = chatId,
    senderId = senderId,
    content = content,
    timestamp = timestamp,
    status = MessageStatus.valueOf(status),
    isEncrypted = isEncrypted
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    chatId = chatId,
    senderId = senderId,
    content = content,
    timestamp = timestamp,
    status = status.name,
    isEncrypted = isEncrypted
)

fun ChatEntity.toDomain(participant: GhostUser, lastMessage: Message?): Chat = Chat(
    id = id,
    participant = participant,
    lastMessage = lastMessage,
    unreadCount = unreadCount,
    updatedAt = updatedAt
)
