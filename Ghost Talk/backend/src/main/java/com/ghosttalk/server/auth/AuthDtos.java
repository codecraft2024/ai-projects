package com.ghosttalk.server.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public final class AuthDtos {

    private AuthDtos() {}

    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 20)
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
            String username,
            @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid mobile number")
            String mobile,
            @NotBlank @Pattern(regexp = "^\\d{6}$", message = "PIN must be exactly 6 digits")
            String pin,
            @Size(max = 64) String displayName,
            @Size(max = 280) String bio,
            @NotBlank String avatarId,
            @NotBlank String fingerprintHash,
            String deviceModel,
            String manufacturer,
            String osVersion,
            String appVersion
    ) {}

    public record LoginPinRequest(
            @NotBlank String identifier,
            @NotBlank @Pattern(regexp = "^\\d{6}$", message = "PIN must be exactly 6 digits")
            String pin,
            @NotBlank String fingerprintHash,
            String deviceModel,
            String manufacturer,
            String osVersion,
            String appVersion
    ) {}

    /** @deprecated Device-only login retained for backward compatibility */
    public record LoginRequest(
            @NotBlank String fingerprintHash,
            String username,
            String deviceModel,
            String manufacturer,
            String osVersion,
            String appVersion
    ) {}

    public record DeviceAccountsRequest(@NotBlank String fingerprintHash) {}

    public record SavedAccountDto(
            String id,
            String username,
            String mobile,
            String displayName,
            String avatarId,
            String lastSeen
    ) {}

    public record DeviceAccountsResponse(
            List<SavedAccountDto> accounts,
            int maxAccounts,
            int remainingSlots
    ) {}

    public record UsernameCheckResponse(boolean available) {}

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
            String mobile,
            String displayName,
            String bio,
            String avatarId,
            String accountCreatedAt,
            boolean online,
            String lastSeen
    ) {}

    public record UpdateProfileRequest(
            @Size(min = 3, max = 20)
            @Pattern(regexp = "^[a-zA-Z0-9_]+$")
            String username,
            @Pattern(regexp = "^\\+?[0-9]{10,15}$")
            String mobile,
            @Size(max = 64) String displayName,
            @Size(max = 280) String bio,
            String avatarId
    ) {}
}
