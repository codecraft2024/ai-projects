package com.twinzy.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.twinzy.persistence.entity.ProfileEntity;

public interface ProfileJpaRepository extends JpaRepository<ProfileEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = "images")
    List<ProfileEntity> findAll();

    @Modifying
    @Query(value = "TRUNCATE TABLE profile_images, profiles RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateAll();
}
