package com.twinzy.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twinzy.config.TwinzyProperties;
import com.twinzy.model.ProfileDataset;
import com.twinzy.repository.ProfileRepository;

@Configuration
public class ProfileLoaderService {

    @Bean
    public ProfileRepository profileRepository(TwinzyProperties properties, ObjectMapper objectMapper) throws IOException {
        Path dataPath = Paths.get(properties.getDataPath()).toAbsolutePath().normalize();
        if (!Files.exists(dataPath)) {
            throw new IllegalStateException("Profile dataset not found at " + dataPath);
        }

        ProfileDataset dataset = objectMapper.readValue(Files.readString(dataPath), ProfileDataset.class);
        return new ProfileRepository(dataset.getProfiles());
    }
}
