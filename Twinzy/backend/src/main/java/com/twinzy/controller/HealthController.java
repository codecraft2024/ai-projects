package com.twinzy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twinzy.config.TwinzyProperties;
import com.twinzy.dto.HealthResponseDto;
import com.twinzy.repository.ProfileRepository;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final ProfileRepository profileRepository;
    private final TwinzyProperties properties;

    public HealthController(ProfileRepository profileRepository, TwinzyProperties properties) {
        this.profileRepository = profileRepository;
        this.properties = properties;
    }

    @GetMapping
    public ResponseEntity<HealthResponseDto> health() {
        return ResponseEntity.ok(new HealthResponseDto(
                true,
                profileRepository.count(),
                properties.getFaceProvider()
        ));
    }
}
