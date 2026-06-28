package com.ghosttalk.server.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, UUID> {

    @Query("""
        SELECT cm FROM ConversationMember cm
        JOIN FETCH cm.conversation
        WHERE cm.user.id = :userId
        ORDER BY cm.conversation.updatedAt DESC
        """)
    List<ConversationMember> findByUserIdOrderByConversationUpdatedAtDesc(UUID userId);

    Optional<ConversationMember> findByConversationIdAndUserId(UUID conversationId, UUID userId);

    @Query("""
        SELECT cm FROM ConversationMember cm
        JOIN ConversationMember cm2 ON cm.conversation.id = cm2.conversation.id
        WHERE cm.user.id = :userId1 AND cm2.user.id = :userId2
        AND cm.conversation.type = 'DIRECT'
        """)
    Optional<ConversationMember> findDirectConversation(UUID userId1, UUID userId2);

    List<ConversationMember> findByConversationId(UUID conversationId);
}
