package com.ghosttalk.core.encryption

/**
 * High-level encryption layer that orchestrates key management,
 * session establishment, and message encryption.
 */
interface MessageEncryptionLayer {
    suspend fun encryptOutgoingMessage(
        plainText: String,
        recipientId: String
    ): EncryptedMessage

    suspend fun decryptIncomingMessage(
        encryptedMessage: EncryptedMessage,
        senderId: String
    ): String
}

data class EncryptedMessage(
    val cipherText: String,
    val sessionId: String,
    val keyId: String,
    val timestamp: Long
)
