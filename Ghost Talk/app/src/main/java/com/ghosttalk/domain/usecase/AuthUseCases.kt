package com.ghosttalk.domain.usecase

import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, avatarId: String): AuthResult =
        authRepository.register(username, avatarId)
}

class LoginWithDeviceUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): AuthResult = authRepository.loginWithDevice()
}

class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.logout()
}

class GetCurrentUserUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.getCurrentUser()
}

class UpdateProfileUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String?, avatarId: String?): Result<GhostUser> =
        authRepository.updateProfile(username, avatarId)
}
