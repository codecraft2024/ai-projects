package com.twinzy.dto;

import com.twinzy.model.FeatureBreakdown;
import com.twinzy.model.Profile;

public class ProfileMatchDto {

    private Profile profile;
    private double overallScore;
    private FeatureBreakdown breakdown;
    private String reason;

    public ProfileMatchDto() {
    }

    public ProfileMatchDto(Profile profile, double overallScore, FeatureBreakdown breakdown, String reason) {
        this.profile = profile;
        this.overallScore = overallScore;
        this.breakdown = breakdown;
        this.reason = reason;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public FeatureBreakdown getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(FeatureBreakdown breakdown) {
        this.breakdown = breakdown;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
