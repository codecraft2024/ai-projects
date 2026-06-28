package com.ghosttalk.core.encryption

/**
 * Abstraction for message encryption/decryption.
 * Future implementation will use Signal Protocol.
 */
interface EncryptionProvider {
    fun encrypt(plainText: String, recipientPublicKey: String): String
    fun decrypt(cipherText: String, senderPublicKey: String): String
}
