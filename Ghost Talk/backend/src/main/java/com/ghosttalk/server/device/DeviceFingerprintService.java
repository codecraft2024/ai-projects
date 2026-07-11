package com.ghosttalk.server.device;

import com.ghosttalk.server.common.BusinessException;
import com.ghosttalk.server.user.User;
import com.ghosttalk.server.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceFingerprintService {

    private final DeviceFingerprintRepository repository;
    private final UserRepository userRepository;
    private final int maxAccountsPerFingerprint;
    private final boolean allowAccountTransfer;

    public DeviceFingerprintService(
            DeviceFingerprintRepository repository,
            UserRepository userRepository,
            @Value("${ghosttalk.device.max-accounts-per-fingerprint}") int maxAccountsPerFingerprint,
            @Value("${ghosttalk.device.allow-account-transfer}") boolean allowAccountTransfer
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.maxAccountsPerFingerprint = maxAccountsPerFingerprint;
        this.allowAccountTransfer = allowAccountTransfer;
    }

    @Transactional
    public DeviceFingerprint resolveDevice(String fingerprintHash, String deviceModel,
                                           String manufacturer, String osVersion, String appVersion) {
        Optional<DeviceFingerprint> existing = repository.findByFingerprintHash(fingerprintHash);
        if (existing.isPresent()) {
            DeviceFingerprint device = existing.get();
            if ("BLOCKED".equals(device.getStatus())) {
                throw new BusinessException("This device has been blocked", 403);
            }
            device.setLastSeen(Instant.now());
            device.setDeviceModel(deviceModel);
            device.setManufacturer(manufacturer);
            device.setOsVersion(osVersion);
            device.setAppVersion(appVersion);
            return repository.save(device);
        }
        DeviceFingerprint device = new DeviceFingerprint();
        device.setFingerprintHash(fingerprintHash);
        device.setDeviceModel(deviceModel);
        device.setManufacturer(manufacturer);
        device.setOsVersion(osVersion);
        device.setAppVersion(appVersion);
        return repository.save(device);
    }

    public void validateRegistrationAllowed(DeviceFingerprint device) {
        int activeAccounts = countActiveAccounts(device);
        if (activeAccounts >= maxAccountsPerFingerprint && !allowAccountTransfer) {
            throw new BusinessException(
                    "Maximum of " + maxAccountsPerFingerprint + " accounts allowed per device.", 409);
        }
    }

    public List<User> findActiveUsersOnDevice(DeviceFingerprint device) {
        return userRepository.findByDeviceFingerprint_IdAndStatus(device.getId(), "ACTIVE");
    }

    public List<String> listUsernamesOnDevice(DeviceFingerprint device) {
        return findActiveUsersOnDevice(device).stream()
                .map(User::getUsername)
                .toList();
    }

    public Optional<User> findUserOnDevice(DeviceFingerprint device, String username) {
        if (username == null || username.isBlank()) {
            List<User> users = findActiveUsersOnDevice(device);
            if (users.size() == 1) {
                return Optional.of(users.getFirst());
            }
            return Optional.empty();
        }
        return userRepository.findByUsernameAndDeviceFingerprint_IdAndStatus(
                username, device.getId(), "ACTIVE");
    }

    public int countActiveAccounts(DeviceFingerprint device) {
        return findActiveUsersOnDevice(device).size();
    }

    @Transactional
    public void syncAccountCount(DeviceFingerprint device) {
        device.setAccountCount(countActiveAccounts(device));
        repository.save(device);
    }

    @Transactional
    public void incrementAccountCount(DeviceFingerprint device) {
        syncAccountCount(device);
        device.setLastLogin(Instant.now());
        repository.save(device);
    }

    @Transactional
    public void blockDevice(String fingerprintHash) {
        DeviceFingerprint device = repository.findByFingerprintHash(fingerprintHash)
                .orElseThrow(() -> new BusinessException("Device not found", 404));
        device.setStatus("BLOCKED");
        repository.save(device);
    }

    @Transactional
    public void unblockDevice(String fingerprintHash) {
        DeviceFingerprint device = repository.findByFingerprintHash(fingerprintHash)
                .orElseThrow(() -> new BusinessException("Device not found", 404));
        device.setStatus("ACTIVE");
        repository.save(device);
    }
}
