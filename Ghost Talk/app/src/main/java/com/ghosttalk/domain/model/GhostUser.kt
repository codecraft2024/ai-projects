package com.ghosttalk.domain.model

data class GhostUser(
    val ghostId: String,
    val nickname: String,
    val avatarResId: String,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null
)

enum class AuthType {
    MOBILE,
    ANONYMOUS
}

sealed class AuthResult {
    data class Success(val user: GhostUser, val token: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
    data object Loading : AuthResult()
}
