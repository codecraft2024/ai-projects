package com.ghosttalk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val ghostId: String,
    val nickname: String,
    val avatarResId: String,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null
)

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val participantId: String,
    val lastMessageId: String? = null,
    val unreadCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isArchived: Boolean = false,
    val type: String = "DIRECT"
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: String,
    val isEncrypted: Boolean = false,
    val replyToId: String? = null,
    val messageType: String = "TEXT",
    val clientMessageId: String? = null
)

@Entity(tableName = "pending_messages")
data class PendingMessageEntity(
    @PrimaryKey val clientMessageId: String,
    val chatId: String,
    val content: String,
    val replyToId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val retryCount: Int = 0
)
