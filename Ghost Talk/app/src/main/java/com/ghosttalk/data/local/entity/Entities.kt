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
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: String,
    val isEncrypted: Boolean = false
)
