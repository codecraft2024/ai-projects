package com.ghosttalk.domain.model

data class SavedAccount(
    val userId: String,
    val username: String,
    val mobile: String? = null,
    val displayName: String,
    val avatarId: String,
    val lastActiveAt: Long = System.currentTimeMillis()
)
