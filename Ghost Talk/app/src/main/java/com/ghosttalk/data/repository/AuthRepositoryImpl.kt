package com.ghosttalk.data.repository

import com.ghosttalk.core.device.DeviceFingerprintManager
import com.ghosttalk.core.session.SessionManager
import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.toEntity
import com.ghosttalk.data.remote.api.AuthApi
import com.ghosttalk.data.remote.dto.LoginRequestDto
import com.ghosttalk.data.remote.dto.RegisterRequestDto
import com.ghosttalk.data.remote.dto.UpdateProfileRequestDto
import com.ghosttalk.data.remote.toDomain
import com.ghosttalk.data.remote.toGhostUser
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
    private val userDao: UserDao,
    private val deviceFingerprintManager: DeviceFingerprintManager
) : AuthRepository {

    override suspend fun register(username: String, avatarId: String): AuthResult {
        return try {
            val response = authApi.register(
                RegisterRequestDto(
                    username = username,
                    avatarId = avatarId,
                    fingerprintHash = deviceFingerprintManager.getFingerprintHash(),
                    deviceModel = deviceFingerprintManager.getDeviceModel(),
                    manufacturer = deviceFingerprintManager.getManufacturer(),
                    osVersion = deviceFingerprintManager.getOsVersion(),
                    appVersion = deviceFingerprintManager.getAppVersion()
                )
            )
            handleAuthResponse(response.body())
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun loginWithDevice(): AuthResult {
        return try {
            val response = authApi.login(
                LoginRequestDto(
                    fingerprintHash = deviceFingerprintManager.getFingerprintHash(),
                    deviceModel = deviceFingerprintManager.getDeviceModel(),
                    manufacturer = deviceFingerprintManager.getManufacturer(),
                    osVersion = deviceFingerprintManager.getOsVersion(),
                    appVersion = deviceFingerprintManager.getAppVersion()
                )
            )
            handleAuthResponse(response.body())
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun logout() {
        try { authApi.logout() } catch (_: Exception) { }
        sessionManager.clearSession()
    }

    override suspend fun updateProfile(username: String?, avatarId: String?): Result<GhostUser> {
        return try {
            val response = authApi.updateProfile(UpdateProfileRequestDto(username, avatarId))
            val body = response.body()
            if (!response.isSuccessful || body?.data == null) {
                return Result.failure(Exception(body?.message ?: "Update failed"))
            }
            val user = body.data!!.toGhostUser()
            userDao.insertUser(user.toEntity())
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<GhostUser?> = sessionManager.currentUser

    override fun isLoggedIn(): Flow<Boolean> = sessionManager.isLoggedIn

    private suspend fun handleAuthResponse(
        body: com.ghosttalk.data.remote.dto.ApiResponse<com.ghosttalk.data.remote.dto.AuthResponseDto>?
    ): AuthResult {
        if (body == null || !body.success || body.data == null) {
            return AuthResult.Error(body?.message ?: "Authentication failed")
        }
        val data = body.data!!
        val user = data.user.toGhostUser()
        userDao.insertUser(user.toEntity())
        sessionManager.saveSession(user, AuthType.DEVICE, data.accessToken, data.refreshToken)
        return AuthResult.Success(user, data.accessToken)
    }
}
