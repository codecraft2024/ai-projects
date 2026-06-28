package com.ghosttalk.server.message;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageStatusRepository extends JpaRepository<MessageStatusEntity, UUID> {
    List<MessageStatusEntity> findByMessageId(UUID messageId);
    Optional<MessageStatusEntity> findByMessageIdAndUserId(UUID messageId, UUID userId);
}
