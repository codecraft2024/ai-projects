package com.ghosttalk.domain.model

data class GhostUser(
    val ghostId: String,
    val nickname: String,
    val avatarResId: String,
    val displayName: String = nickname,
    val bio: String? = null,
    val mobile: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null,
    val accountCreatedAt: String? = null,
    val verified: Boolean = false
)

enum class AuthType {
    PIN
}

sealed class AuthResult {
    data class Success(val user: GhostUser, val token: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
    data object Loading : AuthResult()
}
