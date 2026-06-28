package com.ghosttalk.core.encryption

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpSecureSessionManager @Inject constructor() : SecureSessionManager {
    private val sessions = mutableMapOf<String, SecureSession>()

    override suspend fun establishSession(
        remoteUserId: String,
        remotePublicKey: String
    ): SecureSession {
        val session = SecureSession(
            sessionId = UUID.randomUUID().toString(),
            remoteUserId = remoteUserId,
            establishedAt = System.currentTimeMillis(),
            isActive = true
        )
        sessions[remoteUserId] = session
        return session
    }

    override suspend fun getSession(remoteUserId: String): SecureSession? =
        sessions[remoteUserId]

    override suspend fun invalidateSession(remoteUserId: String) {
        sessions.remove(remoteUserId)
    }
}
