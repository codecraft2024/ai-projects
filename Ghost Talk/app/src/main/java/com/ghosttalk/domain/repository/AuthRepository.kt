package com.ghosttalk.domain.repository

import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.GhostUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(username: String, avatarId: String): AuthResult
    suspend fun loginWithDevice(): AuthResult
    suspend fun logout()
    suspend fun updateProfile(username: String?, avatarId: String?): Result<GhostUser>
    fun getCurrentUser(): Flow<GhostUser?>
    fun isLoggedIn(): Flow<Boolean>
}
