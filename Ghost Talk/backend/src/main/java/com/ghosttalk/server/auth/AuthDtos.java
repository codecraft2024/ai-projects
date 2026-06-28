package com.ghosttalk.server.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public final class AuthDtos {

    private AuthDtos() {}

    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 20)
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
            String username,
            @NotBlank String avatarId,
            @NotBlank String fingerprintHash,
            String deviceModel,
            String manufacturer,
            String osVersion,
            String appVersion
    ) {}

    public record LoginRequest(
            @NotBlank String fingerprintHash,
            String deviceModel,
            String manufacturer,
            String osVersion,
            String appVersion
    ) {}

    public record RefreshTokenRequest(@NotBlank String refreshToken) {}

    public record AuthResponse(
            String accessToken,
            String refreshToken,
            long expiresIn,
            UserDto user
    ) {}

    public record UserDto(
            String id,
            String username,
            String avatarId,
            String accountCreatedAt,
            boolean online,
            String lastSeen
    ) {}

    public record UpdateProfileRequest(
            @Size(min = 3, max = 20)
            @Pattern(regexp = "^[a-zA-Z0-9_]+$")
            String username,
            String avatarId
    ) {}
}
