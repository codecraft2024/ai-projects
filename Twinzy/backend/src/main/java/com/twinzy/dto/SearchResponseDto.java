package com.twinzy.dto;

import java.util.List;

public class SearchResponseDto {

    private String sessionId;
    private String userImageDataUrl;
    private ProfileMatchDto funnyMatch;
    private List<ProfileMatchDto> humanMatches;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserImageDataUrl() {
        return userImageDataUrl;
    }

    public void setUserImageDataUrl(String userImageDataUrl) {
        this.userImageDataUrl = userImageDataUrl;
    }

    public ProfileMatchDto getFunnyMatch() {
        return funnyMatch;
    }

    public void setFunnyMatch(ProfileMatchDto funnyMatch) {
        this.funnyMatch = funnyMatch;
    }

    public List<ProfileMatchDto> getHumanMatches() {
        return humanMatches;
    }

    public void setHumanMatches(List<ProfileMatchDto> humanMatches) {
        this.humanMatches = humanMatches;
    }
}
