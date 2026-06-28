package com.ghosttalk.core.encryption

/**
 * Manages public/private key pairs for E2E encryption.
 * Future: integrate with Android Keystore and Signal key generation.
 */
interface KeyManager {
    suspend fun generateKeyPair(): KeyPair
    suspend fun getPublicKey(): String?
    suspend fun getPrivateKey(): String?
    suspend fun storeKeyPair(keyPair: KeyPair)
    suspend fun rotateKeys(): KeyPair
}

data class KeyPair(
    val publicKey: String,
    val privateKey: String,
    val keyId: String
)
