package com.twinzy.model;

import java.util.List;

public class ProfileDataset {

    private int version;
    private String generatedAt;
    private List<StoredProfile> profiles;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<StoredProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<StoredProfile> profiles) {
        this.profiles = profiles;
    }
}
