package com.ghosttalk.domain.model

data class Chat(
    val id: String,
    val participant: GhostUser,
    val lastMessage: Message?,
    val unreadCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus,
    val isEncrypted: Boolean = false
)

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}
