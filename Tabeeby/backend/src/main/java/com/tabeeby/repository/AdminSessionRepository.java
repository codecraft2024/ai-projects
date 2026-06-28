package com.tabeeby.repository;

import com.tabeeby.model.AdminSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface AdminSessionRepository extends JpaRepository<AdminSession, String> {

    @Modifying
    @Query("DELETE FROM AdminSession s WHERE s.expiresAt < :now")
    int deleteExpired(Instant now);
}
