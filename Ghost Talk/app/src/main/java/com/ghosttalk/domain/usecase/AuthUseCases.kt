package com.ghosttalk.domain.usecase

import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.repository.AuthRepository
import com.ghosttalk.domain.repository.DeviceAccountsInfo
import javax.inject.Inject

class CheckUsernameUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String): Result<Boolean> =
        authRepository.checkUsername(username)
}

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        username: String,
        pin: String,
        avatarId: String,
        mobile: String? = null,
        displayName: String? = null,
        bio: String? = null
    ): AuthResult = authRepository.register(username, pin, avatarId, mobile, displayName, bio)
}

class LoginWithPinUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(identifier: String, pin: String): AuthResult =
        authRepository.loginWithPin(identifier, pin)
}

class GetDeviceAccountsUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<DeviceAccountsInfo> = authRepository.getDeviceAccounts()
}

class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.logout()
}

class ClearActiveSessionUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.clearActiveSession()
}

class GetCurrentUserUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.getCurrentUser()
}

class UpdateProfileUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        username: String? = null,
        avatarId: String? = null,
        mobile: String? = null,
        displayName: String? = null,
        bio: String? = null
    ): Result<GhostUser> = authRepository.updateProfile(username, avatarId, mobile, displayName, bio)
}
