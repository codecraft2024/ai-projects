package com.ghosttalk.core.encryption

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpMessageEncryptionLayer @Inject constructor(
    private val encryptionProvider: EncryptionProvider,
    private val sessionManager: SecureSessionManager
) : MessageEncryptionLayer {

    override suspend fun encryptOutgoingMessage(
        plainText: String,
        recipientId: String
    ): EncryptedMessage {
        val session = sessionManager.getSession(recipientId)
            ?: sessionManager.establishSession(recipientId, "")
        val cipherText = encryptionProvider.encrypt(plainText, "")
        return EncryptedMessage(
            cipherText = cipherText,
            sessionId = session.sessionId,
            keyId = session.sessionId,
            timestamp = System.currentTimeMillis()
        )
    }

    override suspend fun decryptIncomingMessage(
        encryptedMessage: EncryptedMessage,
        senderId: String
    ): String {
        return encryptionProvider.decrypt(encryptedMessage.cipherText, "")
    }
}
