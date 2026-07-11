package com.ghosttalk.core.session

import com.ghosttalk.domain.model.AuthType
import com.ghosttalk.domain.model.GhostUser
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    val currentUser: Flow<GhostUser?>
    val isLoggedIn: Flow<Boolean>
    val hasCompletedOnboarding: Flow<Boolean>

    suspend fun saveSession(
        user: GhostUser,
        authType: AuthType,
        accessToken: String,
        refreshToken: String
    )
    suspend fun updateTokens(accessToken: String, refreshToken: String)
    suspend fun clearSession()
    suspend fun clearActiveSession()
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
}
