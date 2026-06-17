package com.twinzy.dto;

import com.twinzy.model.Profile;

public class RelatedProfileDto {

    private Profile profile;
    private double score;

    public RelatedProfileDto() {
    }

    public RelatedProfileDto(Profile profile, double score) {
        this.profile = profile;
        this.score = score;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
