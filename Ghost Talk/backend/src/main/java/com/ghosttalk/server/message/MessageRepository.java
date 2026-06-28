package com.ghosttalk.server.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("""
        SELECT m FROM Message m
        WHERE m.conversation.id = :conversationId AND m.deletedForAll = false
        ORDER BY m.createdAt DESC
        """)
    Page<Message> findByConversationId(UUID conversationId, Pageable pageable);

    Optional<Message> findByClientMessageId(String clientMessageId);

    @Query("""
        SELECT m FROM Message m
        WHERE m.conversation.id IN :conversationIds
        AND m.createdAt > :since
        ORDER BY m.createdAt ASC
        """)
    List<Message> findSyncMessages(List<UUID> conversationIds, java.time.Instant since);
}
