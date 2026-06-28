package com.ghosttalk.data.repository

import com.ghosttalk.core.session.SessionManager
import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.toDomain
import com.ghosttalk.data.local.toEntity
import com.ghosttalk.data.remote.api.AuthApi
import com.ghosttalk.data.remote.dto.AuthRequestDto
import com.ghosttalk.data.remote.dto.OtpRequestDto
import com.ghosttalk.data.remote.toDomain
import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.AuthType
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String): Result<Unit> = runCatching {
        val response = authApi.sendOtp(OtpRequestDto(phoneNumber))
        if (!response.isSuccessful) throw Exception("Failed to send OTP")
    }

    override suspend fun loginWithMobile(phoneNumber: String, otp: String): AuthResult {
        return try {
            val response = authApi.login(
                AuthRequestDto(
                    phoneNumber = phoneNumber,
                    otp = otp,
                    authType = AuthType.MOBILE.name
                )
            )
            if (!response.isSuccessful || response.body() == null) {
                return AuthResult.Error("Login failed")
            }
            val body = response.body()!!
            if (body.token.isBlank() || body.user.ghostId.isBlank()) {
                return AuthResult.Error("Invalid OTP. Use 123456 for testing.")
            }
            val user = body.user.toDomain()
            userDao.insertUser(user.toEntity())
            sessionManager.saveSession(user, AuthType.MOBILE, body.token)
            AuthResult.Success(user, body.token)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun loginWithNickname(nickname: String, avatarId: String): AuthResult {
        return try {
            val response = authApi.login(
                AuthRequestDto(
                    nickname = nickname,
                    avatarId = avatarId,
                    authType = AuthType.ANONYMOUS.name
                )
            )
            if (!response.isSuccessful || response.body() == null) {
                return AuthResult.Error("Login failed")
            }
            val body = response.body()!!
            val user = body.user.toDomain()
            userDao.insertUser(user.toEntity())
            sessionManager.saveSession(user, AuthType.ANONYMOUS, body.token)
            AuthResult.Success(user, body.token)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override fun getCurrentUser(): Flow<GhostUser?> = sessionManager.currentUser

    override fun isLoggedIn(): Flow<Boolean> = sessionManager.isLoggedIn
}
