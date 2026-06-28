package com.ghosttalk.core.encryption

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpKeyManager @Inject constructor() : KeyManager {
    private var storedKeyPair: KeyPair? = null

    override suspend fun generateKeyPair(): KeyPair {
        val keyPair = KeyPair(
            publicKey = "noop_public_${UUID.randomUUID()}",
            privateKey = "noop_private_${UUID.randomUUID()}",
            keyId = UUID.randomUUID().toString()
        )
        storedKeyPair = keyPair
        return keyPair
    }

    override suspend fun getPublicKey(): String? = storedKeyPair?.publicKey
    override suspend fun getPrivateKey(): String? = storedKeyPair?.privateKey

    override suspend fun storeKeyPair(keyPair: KeyPair) {
        storedKeyPair = keyPair
    }

    override suspend fun rotateKeys(): KeyPair = generateKeyPair()
}
