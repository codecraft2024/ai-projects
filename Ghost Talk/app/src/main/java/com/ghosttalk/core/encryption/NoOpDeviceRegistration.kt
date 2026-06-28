package com.ghosttalk.core.encryption

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpDeviceRegistration @Inject constructor() : DeviceRegistration {
    private var registered = false

    override suspend fun registerDevice(
        publicKey: String,
        preKeys: List<PreKey>
    ): RegistrationResult {
        registered = true
        return RegistrationResult(deviceId = UUID.randomUUID().toString(), success = true)
    }

    override suspend fun isDeviceRegistered(): Boolean = registered
}
