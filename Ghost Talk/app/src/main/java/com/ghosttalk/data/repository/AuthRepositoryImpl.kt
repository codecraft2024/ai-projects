package com.ghosttalk.data.repository

import com.ghosttalk.core.auth.AccountManager
import com.ghosttalk.core.device.DeviceFingerprintManager
import com.ghosttalk.core.network.NetworkErrorMapper
import com.ghosttalk.core.session.SessionManager
import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.toEntity
import com.ghosttalk.data.remote.api.AuthApi
import com.ghosttalk.data.remote.dto.DeviceAccountsRequestDto
import com.ghosttalk.data.remote.dto.LoginPinRequestDto
import com.ghosttalk.data.remote.dto.RegisterRequestDto
import com.ghosttalk.data.remote.dto.UpdateProfileRequestDto
import com.ghosttalk.data.remote.toGhostUser
import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.AuthType
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.model.SavedAccount
import com.ghosttalk.domain.repository.AuthRepository
import com.ghosttalk.domain.repository.DeviceAccountsInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager,
    private val userDao: UserDao,
    private val deviceFingerprintManager: DeviceFingerprintManager,
    private val accountManager: AccountManager
) : AuthRepository {

    override suspend fun checkUsername(username: String): Result<Boolean> {
        return try {
            val response = authApi.checkUsername(username)
            val body = response.body()
            if (!response.isSuccessful || body?.data == null) {
                return Result.failure(Exception(body?.message ?: "Could not verify username"))
            }
            Result.success(body.data!!.available)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkErrorMapper.toUserMessage(e)))
        }
    }

    override suspend fun register(
        username: String,
        pin: String,
        avatarId: String,
        mobile: String?,
        displayName: String?,
        bio: String?
    ): AuthResult {
        return try {
            val response = authApi.register(
                RegisterRequestDto(
                    username = username,
                    mobile = mobile,
                    pin = pin,
                    displayName = displayName,
                    bio = bio,
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
            AuthResult.Error(NetworkErrorMapper.toUserMessage(e))
        }
    }

    override suspend fun loginWithPin(identifier: String, pin: String): AuthResult {
        return try {
            val response = authApi.loginWithPin(
                LoginPinRequestDto(
                    identifier = identifier,
                    pin = pin,
                    fingerprintHash = deviceFingerprintManager.getFingerprintHash(),
                    deviceModel = deviceFingerprintManager.getDeviceModel(),
                    manufacturer = deviceFingerprintManager.getManufacturer(),
                    osVersion = deviceFingerprintManager.getOsVersion(),
                    appVersion = deviceFingerprintManager.getAppVersion()
                )
            )
            handleAuthResponse(response.body())
        } catch (e: Exception) {
            AuthResult.Error(NetworkErrorMapper.toUserMessage(e))
        }
    }

    override suspend fun getDeviceAccounts(): Result<DeviceAccountsInfo> {
        return try {
            val response = authApi.getDeviceAccounts(
                DeviceAccountsRequestDto(deviceFingerprintManager.getFingerprintHash())
            )
            val body = response.body()
            if (!response.isSuccessful || body?.data == null) {
                return Result.failure(Exception(body?.message ?: "Failed to load accounts"))
            }
            val data = body.data!!
            val accounts = data.accounts.map { dto ->
                SavedAccount(
                    userId = dto.id,
                    username = dto.username,
                    mobile = dto.mobile,
                    displayName = dto.displayName ?: dto.username,
                    avatarId = dto.avatarId
                )
            }
            Result.success(
                DeviceAccountsInfo(
                    accounts = accounts,
                    maxAccounts = data.maxAccounts,
                    remainingSlots = data.remainingSlots
                )
            )
        } catch (e: Exception) {
            Result.failure(Exception(NetworkErrorMapper.toUserMessage(e)))
        }
    }

    override suspend fun logout() {
        try { authApi.logout() } catch (_: Exception) { }
        sessionManager.clearActiveSession()
    }

    override suspend fun clearActiveSession() {
        sessionManager.clearActiveSession()
    }

    override suspend fun updateProfile(
        username: String?,
        avatarId: String?,
        mobile: String?,
        displayName: String?,
        bio: String?
    ): Result<GhostUser> {
        return try {
            val response = authApi.updateProfile(
                UpdateProfileRequestDto(username, mobile, displayName, bio, avatarId)
            )
            val body = response.body()
            if (!response.isSuccessful || body?.data == null) {
                return Result.failure(Exception(body?.message ?: "Update failed"))
            }
            val user = body.data!!.toGhostUser()
            userDao.insertUser(user.toEntity())
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(NetworkErrorMapper.toUserMessage(e)))
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
        sessionManager.saveSession(user, AuthType.PIN, data.accessToken, data.refreshToken)
        accountManager.upsertAccount(
            SavedAccount(
                userId = user.ghostId,
                username = user.nickname,
                mobile = user.mobile,
                displayName = user.displayName,
                avatarId = user.avatarResId
            )
        )
        return AuthResult.Success(user, data.accessToken)
    }
}
