package com.ghosttalk.server.device;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DeviceFingerprintRepository extends JpaRepository<DeviceFingerprint, UUID> {
    Optional<DeviceFingerprint> findByFingerprintHash(String fingerprintHash);
}
