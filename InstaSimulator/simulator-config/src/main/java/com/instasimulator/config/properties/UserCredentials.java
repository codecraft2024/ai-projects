package com.instasimulator.config.properties;

import lombok.Data;

/**
 * Credentials entry from a user pool YAML file.
 */
@Data
public class UserCredentials {

    private String userId;
    private String username;
    private String password;
    private String email;
}
