package com.patienthub.service;

import com.patienthub.dto.LoginRequest;
import com.patienthub.dto.LoginResponse;
import com.patienthub.exception.UnauthorizedException;
import com.patienthub.model.AdminUser;
import com.patienthub.repository.AdminUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenStore authTokenStore;

    public AuthService(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder,
            AuthTokenStore authTokenStore) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authTokenStore = authTokenStore;
    }

    public LoginResponse login(LoginRequest request) {
        AdminUser user = adminUserRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        String token = authTokenStore.issueToken(user.getUsername());
        return new LoginResponse(user.getUsername(), token);
    }

    public void logout(String token) {
        authTokenStore.revoke(token);
    }

    public String requireUsername(String token) {
        return authTokenStore.resolveUsername(token)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));
    }
}
