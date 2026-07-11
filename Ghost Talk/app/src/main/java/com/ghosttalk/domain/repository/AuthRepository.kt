package com.ghosttalk.domain.repository

import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.model.SavedAccount
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun checkUsername(username: String): Result<Boolean>
    suspend fun register(
        username: String,
        pin: String,
        avatarId: String,
        mobile: String? = null,
        displayName: String? = null,
        bio: String? = null
    ): AuthResult
    suspend fun loginWithPin(identifier: String, pin: String): AuthResult
    suspend fun getDeviceAccounts(): Result<DeviceAccountsInfo>
    suspend fun logout()
    suspend fun clearActiveSession()
    suspend fun updateProfile(
        username: String? = null,
        avatarId: String? = null,
        mobile: String? = null,
        displayName: String? = null,
        bio: String? = null
    ): Result<GhostUser>
    fun getCurrentUser(): Flow<GhostUser?>
    fun isLoggedIn(): Flow<Boolean>
}

data class DeviceAccountsInfo(
    val accounts: List<SavedAccount>,
    val maxAccounts: Int,
    val remainingSlots: Int
)
