package com.ghosttalk.server.admin;

import com.ghosttalk.server.common.ApiResponse;
import com.ghosttalk.server.device.DeviceFingerprintRepository;
import com.ghosttalk.server.device.DeviceFingerprintService;
import com.ghosttalk.server.user.User;
import com.ghosttalk.server.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DeviceFingerprintService deviceService;
    private final DeviceFingerprintRepository deviceRepository;

    public AdminController(UserRepository userRepository,
                           DeviceFingerprintService deviceService,
                           DeviceFingerprintRepository deviceRepository) {
        this.userRepository = userRepository;
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping("/devices/{fingerprintHash}/block")
    public ApiResponse<Void> blockDevice(@PathVariable String fingerprintHash) {
        deviceService.blockDevice(fingerprintHash);
        return ApiResponse.ok("Device blocked", null);
    }

    @PostMapping("/devices/{fingerprintHash}/unblock")
    public ApiResponse<Void> unblockDevice(@PathVariable String fingerprintHash) {
        deviceService.unblockDevice(fingerprintHash);
        return ApiResponse.ok("Device unblocked", null);
    }

    @PostMapping("/users/{userId}/ban")
    public ApiResponse<Void> banUser(@PathVariable UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus("BANNED");
        userRepository.save(user);
        return ApiResponse.ok("User banned", null);
    }

    @PostMapping("/users/{userId}/unban")
    public ApiResponse<Void> unbanUser(@PathVariable UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus("ACTIVE");
        userRepository.save(user);
        return ApiResponse.ok("User unbanned", null);
    }

    @DeleteMapping("/users/{userId}")
    public ApiResponse<Void> deleteAccount(@PathVariable UUID userId) {
        userRepository.deleteById(userId);
        return ApiResponse.ok("Account deleted", null);
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> stats() {
        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return ApiResponse.ok(Map.of(
                "activeUsers", userRepository.countActiveUsers(),
                "dailyRegistrations", userRepository.countRegistrationsSince(today),
                "totalDevices", deviceRepository.count()
        ));
    }
}
