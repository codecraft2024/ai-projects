package com.ghosttalk.server.auth;

import com.ghosttalk.server.common.BusinessException;
import com.ghosttalk.server.common.ResourceNotFoundException;
import com.ghosttalk.server.device.DeviceFingerprint;
import com.ghosttalk.server.device.DeviceFingerprintService;
import com.ghosttalk.server.security.JwtService;
import com.ghosttalk.server.user.User;
import com.ghosttalk.server.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DeviceFingerprintService deviceService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final int maxAccountsPerFingerprint;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       DeviceFingerprintService deviceService,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       @Value("${ghosttalk.device.max-accounts-per-fingerprint}") int maxAccountsPerFingerprint) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.deviceService = deviceService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.maxAccountsPerFingerprint = maxAccountsPerFingerprint;
    }

    public AuthDtos.UsernameCheckResponse checkUsername(String username) {
        if (username == null || username.isBlank()) {
            return new AuthDtos.UsernameCheckResponse(false);
        }
        return new AuthDtos.UsernameCheckResponse(!userRepository.existsByUsername(username.trim()));
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        String username = request.username().trim();
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Username already taken");
        }
        String mobile = normalizeMobile(request.mobile());
        if (mobile != null && userRepository.existsByMobile(mobile)) {
            throw new BusinessException("Mobile number already registered");
        }

        DeviceFingerprint device = deviceService.resolveDevice(
                request.fingerprintHash(), request.deviceModel(),
                request.manufacturer(), request.osVersion(), request.appVersion());
        deviceService.validateRegistrationAllowed(device);

        User user = new User();
        user.setUsername(username);
        user.setMobile(mobile);
        user.setPinHash(passwordEncoder.encode(request.pin()));
        user.setDisplayName(
                request.displayName() != null && !request.displayName().isBlank()
                        ? request.displayName().trim()
                        : username
        );
        user.setBio(request.bio());
        user.setAvatarId(request.avatarId());
        user.setDeviceFingerprint(device);
        user = userRepository.save(user);

        deviceService.incrementAccountCount(device);
        return buildAuthResponse(user);
    }

    @Transactional
    public AuthDtos.AuthResponse loginWithPin(AuthDtos.LoginPinRequest request) {
        DeviceFingerprint device = deviceService.resolveDevice(
                request.fingerprintHash(), request.deviceModel(),
                request.manufacturer(), request.osVersion(), request.appVersion());

        User user = resolveUserByIdentifier(request.identifier())
                .orElseThrow(() -> new BusinessException("Account not found", 404));

        verifyUserOnDevice(user, device);
        verifyPin(user, request.pin());

        return activateUser(user, device);
    }

    @Transactional
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        DeviceFingerprint device = deviceService.resolveDevice(
                request.fingerprintHash(), request.deviceModel(),
                request.manufacturer(), request.osVersion(), request.appVersion());

        List<User> accounts = deviceService.findActiveUsersOnDevice(device);
        if (accounts.isEmpty()) {
            throw new BusinessException("No account found for this device. Please register.", 404);
        }

        User user = deviceService.findUserOnDevice(device, request.username())
                .orElseThrow(() -> new BusinessException(
                        accounts.size() > 1
                                ? "Select an account to continue"
                                : "No account found for this device. Please register.",
                        accounts.size() > 1 ? 409 : 404));

        if (user.getPinHash() != null && !user.getPinHash().isBlank()) {
            throw new BusinessException("PIN required. Please sign in with your PIN.", 401);
        }

        return activateUser(user, device);
    }

    public AuthDtos.DeviceAccountsResponse listDeviceAccounts(AuthDtos.DeviceAccountsRequest request) {
        DeviceFingerprint device = deviceService.resolveDevice(
                request.fingerprintHash(), null, null, null, null);
        List<AuthDtos.SavedAccountDto> accounts = deviceService.findActiveUsersOnDevice(device).stream()
                .map(this::toSavedAccountDto)
                .toList();
        int active = accounts.size();
        return new AuthDtos.DeviceAccountsResponse(
                accounts, maxAccountsPerFingerprint, Math.max(0, maxAccountsPerFingerprint - active));
    }

    @Transactional
    public AuthDtos.AuthResponse refresh(AuthDtos.RefreshTokenRequest request) {
        String hash = hashToken(request.refreshToken());
        RefreshToken stored = refreshTokenRepository.findByTokenHashAndRevokedFalse(hash)
                .orElseThrow(() -> new BusinessException("Invalid refresh token", 401));

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("Refresh token expired", 401);
        }

        User user = userRepository.findById(stored.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);
        return buildAuthResponse(user);
    }

    @Transactional
    public void logout(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setOnline(false);
        user.setLastSeen(Instant.now());
        userRepository.save(user);
    }

    @Transactional
    public AuthDtos.UserDto updateProfile(UUID userId, AuthDtos.UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.username() != null && !request.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.username())) {
                throw new BusinessException("Username already taken");
            }
            user.setUsername(request.username());
        }
        if (request.mobile() != null) {
            String mobile = normalizeMobile(request.mobile());
            if (mobile != null && !mobile.equals(user.getMobile())
                    && userRepository.existsByMobile(mobile)) {
                throw new BusinessException("Mobile number already registered");
            }
            user.setMobile(mobile);
        }
        if (request.displayName() != null) {
            user.setDisplayName(request.displayName().trim());
        }
        if (request.bio() != null) {
            user.setBio(request.bio());
        }
        if (request.avatarId() != null) {
            user.setAvatarId(request.avatarId());
        }
        user.setUpdatedAt(Instant.now());
        return toUserDto(userRepository.save(user));
    }

    public AuthDtos.UserDto getProfile(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private AuthDtos.AuthResponse activateUser(User user, DeviceFingerprint device) {
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException("Account is suspended", 403);
        }
        user.setOnline(true);
        user.setLastSeen(Instant.now());
        userRepository.save(user);
        device.setLastLogin(Instant.now());
        return buildAuthResponse(user);
    }

    private void verifyUserOnDevice(User user, DeviceFingerprint device) {
        if (user.getDeviceFingerprint() == null
                || !user.getDeviceFingerprint().getId().equals(device.getId())) {
            throw new BusinessException("This account is not registered on this device", 403);
        }
    }

    private void verifyPin(User user, String pin) {
        if (user.getPinHash() == null || user.getPinHash().isBlank()) {
            throw new BusinessException("PIN not set for this account", 401);
        }
        if (!passwordEncoder.matches(pin, user.getPinHash())) {
            throw new BusinessException("Invalid PIN", 401);
        }
    }

    private java.util.Optional<User> resolveUserByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return java.util.Optional.empty();
        }
        String value = identifier.trim();
        if (isPhoneNumber(value)) {
            String mobile = normalizeMobile(value);
            if (mobile != null) {
                return userRepository.findByMobile(mobile);
            }
        }
        return userRepository.findByUsername(value);
    }

    static boolean isPhoneNumber(String value) {
        long digitCount = value.chars().filter(Character::isDigit).count();
        return digitCount >= 10 && digitCount >= value.replace(" ", "").length() * 0.6;
    }

    static String normalizeMobile(String mobile) {
        if (mobile == null || mobile.isBlank()) return null;
        String digits = mobile.replaceAll("[^0-9+]", "");
        if (digits.startsWith("+")) {
            return digits;
        }
        return digits.isEmpty() ? null : digits;
    }

    private AuthDtos.SavedAccountDto toSavedAccountDto(User user) {
        return new AuthDtos.SavedAccountDto(
                user.getId().toString(),
                user.getUsername(),
                maskMobile(user.getMobile()),
                user.getDisplayName(),
                user.getAvatarId(),
                user.getLastSeen() != null ? user.getLastSeen().toString() : null
        );
    }

    static String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 4) return null;
        return "••••" + mobile.substring(mobile.length() - 4);
    }

    private AuthDtos.AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        storeRefreshToken(user.getId(), refreshToken);
        return new AuthDtos.AuthResponse(
                accessToken,
                refreshToken,
                900,
                toUserDto(user)
        );
    }

    private void storeRefreshToken(UUID userId, String token) {
        RefreshToken entity = new RefreshToken();
        entity.setUserId(userId);
        entity.setTokenHash(hashToken(token));
        entity.setExpiresAt(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()));
        refreshTokenRepository.save(entity);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private AuthDtos.UserDto toUserDto(User user) {
        return new AuthDtos.UserDto(
                user.getId().toString(),
                user.getUsername(),
                maskMobile(user.getMobile()),
                user.getDisplayName(),
                user.getBio(),
                user.getAvatarId(),
                user.getAccountCreatedAt().toString(),
                user.isOnline(),
                user.getLastSeen() != null ? user.getLastSeen().toString() : null
        );
    }
}
