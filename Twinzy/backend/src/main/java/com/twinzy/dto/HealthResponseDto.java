package com.twinzy.dto;

public class HealthResponseDto {

    private boolean ok;
    private int profiles;
    private String provider;

    public HealthResponseDto(boolean ok, int profiles, String provider) {
        this.ok = ok;
        this.profiles = profiles;
        this.provider = provider;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getProfiles() {
        return profiles;
    }

    public void setProfiles(int profiles) {
        this.profiles = profiles;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
