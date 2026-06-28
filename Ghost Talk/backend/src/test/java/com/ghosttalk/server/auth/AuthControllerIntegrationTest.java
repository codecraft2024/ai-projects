package com.ghosttalk.server.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerAndLogin() throws Exception {
        String fingerprint = "test_fingerprint_hash_abc123";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "username": "TestGhost",
                              "avatarId": "ghost_1",
                              "fingerprintHash": "%s",
                              "deviceModel": "Pixel",
                              "manufacturer": "Google",
                              "osVersion": "14",
                              "appVersion": "1.0.0"
                            }
                            """.formatted(fingerprint)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "fingerprintHash": "%s",
                              "deviceModel": "Pixel"
                            }
                            """.formatted(fingerprint)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.username").value("TestGhost"));
    }
}
