package com.twinzy.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.twinzy.model.FeatureBreakdown;
import com.twinzy.model.ProfileImage;
import com.twinzy.model.StoredProfile;
import com.twinzy.persistence.entity.ProfileEntity;
import com.twinzy.persistence.entity.ProfileImageEntity;

public final class ProfileEntityMapper {

    private ProfileEntityMapper() {
    }

    public static ProfileEntity toEntity(StoredProfile profile) {
        ProfileEntity entity = new ProfileEntity();
        entity.setId(UUID.fromString(profile.getId()));
        entity.setSlug(profile.getSlug());
        entity.setFullName(profile.getFullName());
        entity.setGender(profile.getGender());
        entity.setDateOfBirth(profile.getDateOfBirth());
        entity.setNationality(profile.getNationality());
        entity.setCountry(profile.getCountry());
        entity.setCity(profile.getCity());
        entity.setOccupation(profile.getOccupation());
        entity.setBiography(profile.getBiography());
        entity.setHeightCm(profile.getHeightCm());
        entity.setEyeColor(profile.getEyeColor());
        entity.setHairColor(profile.getHairColor());
        entity.setEthnicityCategory(profile.getEthnicityCategory());
        entity.setPublicFigureCategory(profile.getPublicFigureCategory());
        entity.setInstagramUrl(profile.getInstagramUrl());
        entity.setXUrl(profile.getXUrl());
        entity.setWebsiteUrl(profile.getWebsiteUrl());
        entity.setPopularityScore(profile.getPopularityScore());
        entity.setFunnyObject(profile.isFunnyObject());
        entity.setTags(profile.getTags() == null ? List.of() : profile.getTags());
        entity.setFaceEmbedding(profile.getFaceEmbedding() == null ? List.of() : profile.getFaceEmbedding());
        entity.setStoredFeatures(profile.getStoredFeatures());

        List<ProfileImageEntity> images = new ArrayList<>();
        if (profile.getImages() != null) {
            for (ProfileImage image : profile.getImages()) {
                ProfileImageEntity imageEntity = new ProfileImageEntity();
                imageEntity.setId(image.getId());
                imageEntity.setProfile(entity);
                imageEntity.setUrl(image.getUrl() == null ? null : image.getUrl().trim());
                imageEntity.setAlt(image.getAlt());
                imageEntity.setPrimaryImage(image.isPrimary());
                imageEntity.setWidth(image.getWidth());
                imageEntity.setHeight(image.getHeight());
                images.add(imageEntity);
            }
        }
        entity.setImages(images);
        return entity;
    }

    public static StoredProfile toStoredProfile(ProfileEntity entity) {
        StoredProfile profile = new StoredProfile();
        profile.setId(entity.getId().toString());
        profile.setSlug(entity.getSlug());
        profile.setFullName(entity.getFullName());
        profile.setGender(entity.getGender());
        profile.setDateOfBirth(entity.getDateOfBirth());
        profile.setNationality(entity.getNationality());
        profile.setCountry(entity.getCountry());
        profile.setCity(entity.getCity());
        profile.setOccupation(entity.getOccupation());
        profile.setBiography(entity.getBiography());
        profile.setHeightCm(entity.getHeightCm());
        profile.setEyeColor(entity.getEyeColor());
        profile.setHairColor(entity.getHairColor());
        profile.setEthnicityCategory(entity.getEthnicityCategory());
        profile.setPublicFigureCategory(entity.getPublicFigureCategory());
        profile.setInstagramUrl(entity.getInstagramUrl());
        profile.setXUrl(entity.getXUrl());
        profile.setWebsiteUrl(entity.getWebsiteUrl());
        profile.setPopularityScore(entity.getPopularityScore());
        profile.setFunnyObject(entity.isFunnyObject());
        profile.setTags(entity.getTags());
        profile.setFaceEmbedding(entity.getFaceEmbedding());
        profile.setStoredFeatures(entity.getStoredFeatures() == null ? new FeatureBreakdown() : entity.getStoredFeatures());

        List<ProfileImage> images = new ArrayList<>();
        for (ProfileImageEntity imageEntity : entity.getImages()) {
            ProfileImage image = new ProfileImage();
            image.setId(imageEntity.getId());
            image.setUrl(imageEntity.getUrl() == null ? null : imageEntity.getUrl().trim());
            image.setAlt(imageEntity.getAlt());
            image.setPrimary(imageEntity.isPrimaryImage());
            image.setWidth(imageEntity.getWidth());
            image.setHeight(imageEntity.getHeight());
            images.add(image);
        }
        profile.setImages(images);
        return profile;
    }
}
