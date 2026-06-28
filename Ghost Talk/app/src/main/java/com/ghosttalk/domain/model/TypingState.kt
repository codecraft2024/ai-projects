package com.ghosttalk.domain.model

data class TypingState(
    val chatId: String,
    val userId: String,
    val isTyping: Boolean
)

data class OnlineStatus(
    val userId: String,
    val isOnline: Boolean,
    val lastSeen: Long? = null
)
