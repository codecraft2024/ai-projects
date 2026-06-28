package com.twinzy.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.twinzy.model.FeatureBreakdown;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public class ProfileEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String gender;

    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String occupation;

    @Column(nullable = false, columnDefinition = "text")
    private String biography;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "eye_color", nullable = false)
    private String eyeColor;

    @Column(name = "hair_color", nullable = false)
    private String hairColor;

    @Column(name = "ethnicity_category", nullable = false)
    private String ethnicityCategory;

    @Column(name = "public_figure_category", nullable = false)
    private String publicFigureCategory;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "x_url")
    private String xUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "popularity_score", nullable = false)
    private double popularityScore;

    @Column(name = "is_funny_object", nullable = false)
    private boolean funnyObject;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<String> tags = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "face_embedding", columnDefinition = "jsonb", nullable = false)
    private List<Double> faceEmbedding = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stored_features", columnDefinition = "jsonb", nullable = false)
    private FeatureBreakdown storedFeatures;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileImageEntity> images = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEthnicityCategory() {
        return ethnicityCategory;
    }

    public void setEthnicityCategory(String ethnicityCategory) {
        this.ethnicityCategory = ethnicityCategory;
    }

    public String getPublicFigureCategory() {
        return publicFigureCategory;
    }

    public void setPublicFigureCategory(String publicFigureCategory) {
        this.publicFigureCategory = publicFigureCategory;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getXUrl() {
        return xUrl;
    }

    public void setXUrl(String xUrl) {
        this.xUrl = xUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public double getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(double popularityScore) {
        this.popularityScore = popularityScore;
    }

    public boolean isFunnyObject() {
        return funnyObject;
    }

    public void setFunnyObject(boolean funnyObject) {
        this.funnyObject = funnyObject;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Double> getFaceEmbedding() {
        return faceEmbedding;
    }

    public void setFaceEmbedding(List<Double> faceEmbedding) {
        this.faceEmbedding = faceEmbedding;
    }

    public FeatureBreakdown getStoredFeatures() {
        return storedFeatures;
    }

    public void setStoredFeatures(FeatureBreakdown storedFeatures) {
        this.storedFeatures = storedFeatures;
    }

    public List<ProfileImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ProfileImageEntity> images) {
        this.images = images;
    }
}
