package com.ghosttalk.domain.repository

import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.AuthType
import com.ghosttalk.domain.model.GhostUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithMobile(phoneNumber: String, otp: String): AuthResult
    suspend fun sendOtp(phoneNumber: String): Result<Unit>
    suspend fun loginWithNickname(nickname: String, avatarId: String): AuthResult
    suspend fun logout()
    fun getCurrentUser(): Flow<GhostUser?>
    fun isLoggedIn(): Flow<Boolean>
}

interface AuthProvider {
  suspend fun authenticate(credentials: Map<String, String>): AuthResult
  val authType: AuthType
}
