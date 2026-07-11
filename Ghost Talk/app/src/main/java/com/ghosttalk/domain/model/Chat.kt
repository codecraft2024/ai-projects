package com.ghosttalk.domain.model

data class Chat(
    val id: String,
    val participant: GhostUser,
    val lastMessage: Message?,
    val unreadCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isArchived: Boolean = false,
    val type: String = "DIRECT"
)

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderUsername: String = "",
    val content: String,
    val timestamp: Long,
    val status: MessageStatus,
    val isEncrypted: Boolean = false,
    val replyToId: String? = null,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false,
    val messageType: String = "TEXT"
)

enum class MessageStatus {
    PENDING,
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}
