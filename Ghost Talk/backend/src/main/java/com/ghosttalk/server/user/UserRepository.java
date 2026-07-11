package com.ghosttalk.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findByMobile(String mobile);
    boolean existsByMobile(String mobile);

    @Query("""
            SELECT u FROM User u WHERE u.status = 'ACTIVE' AND (
                LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(u.displayName) LIKE LOWER(CONCAT('%', :query, '%'))
                OR u.mobile LIKE CONCAT('%', :query, '%')
            )
            """)
    List<User> searchActiveUsers(String query);

    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :since")
    long countRegistrationsSince(Instant since);

    List<User> findByUsernameContainingIgnoreCaseAndStatus(String query, String status);

    List<User> findByDeviceFingerprint_IdAndStatus(UUID deviceFingerprintId, String status);

    Optional<User> findByUsernameAndDeviceFingerprint_IdAndStatus(
            String username, UUID deviceFingerprintId, String status);
}
