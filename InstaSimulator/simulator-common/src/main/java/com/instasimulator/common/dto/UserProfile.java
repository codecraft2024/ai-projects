package com.instasimulator.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authenticated user profile snapshot.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private String userId;
    private String username;
    private String email;
    private String phone;
    private String fullName;
}
