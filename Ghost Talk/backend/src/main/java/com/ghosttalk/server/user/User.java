package com.ghosttalk.server.user;

import com.ghosttalk.server.device.DeviceFingerprint;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(name = "avatar_id", nullable = false, length = 32)
    private String avatarId = "ghost_1";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_fingerprint_id")
    private DeviceFingerprint deviceFingerprint;

    @Column(name = "account_created_at", nullable = false)
    private Instant accountCreatedAt = Instant.now();

    @Column(name = "last_seen")
    private Instant lastSeen;

    @Column(name = "is_online", nullable = false)
    private boolean online = false;

    @Column(nullable = false, length = 32)
    private String status = "ACTIVE";

    @Column(name = "fcm_token", length = 512)
    private String fcmToken;

    @Column(length = 20)
    private String mobile;

    @Column(name = "pin_hash", length = 72)
    private String pinHash;

    @Column(name = "display_name", length = 64)
    private String displayName;

    @Column(length = 280)
    private String bio;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAvatarId() { return avatarId; }
    public void setAvatarId(String avatarId) { this.avatarId = avatarId; }
    public DeviceFingerprint getDeviceFingerprint() { return deviceFingerprint; }
    public void setDeviceFingerprint(DeviceFingerprint deviceFingerprint) { this.deviceFingerprint = deviceFingerprint; }
    public Instant getAccountCreatedAt() { return accountCreatedAt; }
    public void setAccountCreatedAt(Instant accountCreatedAt) { this.accountCreatedAt = accountCreatedAt; }
    public Instant getLastSeen() { return lastSeen; }
    public void setLastSeen(Instant lastSeen) { this.lastSeen = lastSeen; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getPinHash() { return pinHash; }
    public void setPinHash(String pinHash) { this.pinHash = pinHash; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
