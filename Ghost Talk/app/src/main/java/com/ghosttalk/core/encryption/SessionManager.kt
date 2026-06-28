package com.ghosttalk.core.encryption

/**
 * Establishes and manages secure sessions between devices.
 * Future: Signal Protocol session establishment (X3DH, Double Ratchet).
 */
interface SecureSessionManager {
    suspend fun establishSession(remoteUserId: String, remotePublicKey: String): SecureSession
    suspend fun getSession(remoteUserId: String): SecureSession?
    suspend fun invalidateSession(remoteUserId: String)
}

data class SecureSession(
    val sessionId: String,
    val remoteUserId: String,
    val establishedAt: Long,
    val isActive: Boolean
)
