package com.ghosttalk.server.auth;

import com.ghosttalk.server.common.BusinessException;
import com.ghosttalk.server.common.ResourceNotFoundException;
import com.ghosttalk.server.device.DeviceFingerprint;
import com.ghosttalk.server.device.DeviceFingerprintService;
import com.ghosttalk.server.security.JwtService;
import com.ghosttalk.server.user.User;
import com.ghosttalk.server.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DeviceFingerprintService deviceService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       DeviceFingerprintService deviceService,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.deviceService = deviceService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Username already taken");
        }
        DeviceFingerprint device = deviceService.resolveDevice(
                request.fingerprintHash(), request.deviceModel(),
                request.manufacturer(), request.osVersion(), request.appVersion());
        deviceService.validateRegistrationAllowed(device);

        User user = new User();
        user.setUsername(request.username());
        user.setAvatarId(request.avatarId());
        user.setDeviceFingerprint(device);
        user = userRepository.save(user);

        deviceService.incrementAccountCount(device);
        return buildAuthResponse(user);
    }

    @Transactional
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        DeviceFingerprint device = deviceService.resolveDevice(
                request.fingerprintHash(), request.deviceModel(),
                request.manufacturer(), request.osVersion(), request.appVersion());

        User user = deviceService.findExistingUser(device)
                .orElseThrow(() -> new BusinessException("No account found for this device. Please register.", 404));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException("Account is suspended", 403);
        }

        user.setOnline(true);
        user.setLastSeen(Instant.now());
        userRepository.save(user);
        device.setLastLogin(Instant.now());

        return buildAuthResponse(user);
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
                user.getAvatarId(),
                user.getAccountCreatedAt().toString(),
                user.isOnline(),
                user.getLastSeen() != null ? user.getLastSeen().toString() : null
        );
    }
}
