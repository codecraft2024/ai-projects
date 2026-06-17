package com.twinzy.dto;

import java.util.List;

import com.twinzy.model.Profile;

public class ProfileDetailResponseDto {

    private Profile profile;
    private List<RelatedProfileDto> related;
    private ProfileMatchDto match;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<RelatedProfileDto> getRelated() {
        return related;
    }

    public void setRelated(List<RelatedProfileDto> related) {
        this.related = related;
    }

    public ProfileMatchDto getMatch() {
        return match;
    }

    public void setMatch(ProfileMatchDto match) {
        this.match = match;
    }
}
