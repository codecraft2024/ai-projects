package com.ghosttalk.core.encryption

/**
 * Registers device with backend for E2E key exchange.
 * Future: upload pre-keys and signed pre-keys to server.
 */
interface DeviceRegistration {
    suspend fun registerDevice(publicKey: String, preKeys: List<PreKey>): RegistrationResult
    suspend fun isDeviceRegistered(): Boolean
}

data class PreKey(
    val keyId: String,
    val publicKey: String
)

data class RegistrationResult(
    val deviceId: String,
    val success: Boolean
)
