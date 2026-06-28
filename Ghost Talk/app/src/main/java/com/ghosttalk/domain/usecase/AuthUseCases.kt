package com.ghosttalk.domain.usecase

import com.ghosttalk.domain.model.AuthResult
import com.ghosttalk.domain.repository.AuthRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String): Result<Unit> =
        authRepository.sendOtp(phoneNumber)
}

class LoginWithMobileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, otp: String): AuthResult =
        authRepository.loginWithMobile(phoneNumber, otp)
}

class LoginWithNicknameUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(nickname: String, avatarId: String): AuthResult =
        authRepository.loginWithNickname(nickname, avatarId)
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.logout()
}

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getCurrentUser()
}
