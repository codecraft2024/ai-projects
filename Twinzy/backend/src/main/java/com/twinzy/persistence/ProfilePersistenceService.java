package com.twinzy.persistence;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twinzy.model.StoredProfile;
import com.twinzy.persistence.entity.ProfileEntity;

@Service
public class ProfilePersistenceService {

    private final ProfileJpaRepository profileJpaRepository;

    public ProfilePersistenceService(ProfileJpaRepository profileJpaRepository) {
        this.profileJpaRepository = profileJpaRepository;
    }

    @Transactional(readOnly = true)
    public List<StoredProfile> loadAllProfiles() {
        return profileJpaRepository.findAll().stream()
                .map(ProfileEntityMapper::toStoredProfile)
                .toList();
    }

    @Transactional
    public void replaceAllProfiles(List<StoredProfile> profiles) {
        profileJpaRepository.truncateAll();
        profileJpaRepository.flush();

        List<ProfileEntity> entities = profiles.stream()
                .map(ProfileEntityMapper::toEntity)
                .toList();

        profileJpaRepository.saveAll(entities);
    }

    @Transactional(readOnly = true)
    public long countProfiles() {
        return profileJpaRepository.count();
    }
}
