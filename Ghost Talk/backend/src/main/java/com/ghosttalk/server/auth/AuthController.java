package com.ghosttalk.server.auth;

import com.ghosttalk.server.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<AuthDtos.AuthResponse> register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
        return ApiResponse.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthDtos.AuthResponse> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthDtos.AuthResponse> refresh(@Valid @RequestBody AuthDtos.RefreshTokenRequest request) {
        return ApiResponse.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(Authentication authentication) {
        authService.logout((UUID) authentication.getPrincipal());
        return ApiResponse.ok("Logged out", null);
    }

    @GetMapping("/me")
    public ApiResponse<AuthDtos.UserDto> me(Authentication authentication) {
        return ApiResponse.ok(authService.getProfile((UUID) authentication.getPrincipal()));
    }

    @PatchMapping("/me")
    public ApiResponse<AuthDtos.UserDto> updateProfile(
            Authentication authentication,
            @Valid @RequestBody AuthDtos.UpdateProfileRequest request
    ) {
        return ApiResponse.ok(authService.updateProfile((UUID) authentication.getPrincipal(), request));
    }
}
