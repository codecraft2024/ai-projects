package com.twinzy.dto;

import com.twinzy.model.Profile;

public class ProfileSummaryDto {

    private Profile profile;
    private String primaryImageUrl;

    public ProfileSummaryDto() {
    }

    public ProfileSummaryDto(Profile profile, String primaryImageUrl) {
        this.profile = profile;
        this.primaryImageUrl = primaryImageUrl;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }
}
