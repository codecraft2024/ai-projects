package com.twinzy.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.twinzy.persistence.ProfilePersistenceService;
import com.twinzy.repository.ProfileRepository;

@Configuration
public class ProfileLoaderService {

    @Bean
    public ProfileRepository profileRepository(ProfilePersistenceService persistenceService) {
        return new ProfileRepository(persistenceService.loadAllProfiles());
    }
}
