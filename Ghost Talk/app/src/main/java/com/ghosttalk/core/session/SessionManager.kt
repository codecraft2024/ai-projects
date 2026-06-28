package com.ghosttalk.core.session

import com.ghosttalk.domain.model.AuthType
import com.ghosttalk.domain.model.GhostUser
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    val currentUser: Flow<GhostUser?>
    val isLoggedIn: Flow<Boolean>
    val hasCompletedOnboarding: Flow<Boolean>

    suspend fun saveSession(user: GhostUser, authType: AuthType, token: String)
    suspend fun clearSession()
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun getAuthToken(): String?
}
