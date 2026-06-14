package com.twinzy.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Profile {

    private String id;
    private String slug;
    private String fullName;
    private String gender;
    private String dateOfBirth;
    private String nationality;
    private String country;
    private String city;
    private String occupation;
    private String biography;
    private Integer heightCm;
    private String eyeColor;
    private String hairColor;
    private String ethnicityCategory;
    private String publicFigureCategory;
    private String instagramUrl;
    private String xUrl;
    private String websiteUrl;
    private List<ProfileImage> images;
    private List<String> tags;
    @JsonIgnore
    private List<Double> faceEmbedding;
    private double popularityScore;
    @JsonProperty("isFunnyObject")
    private boolean funnyObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<ProfileImage> getImages() {
        return images;
    }

    public void setImages(List<ProfileImage> images) {
        this.images = images;
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
}
