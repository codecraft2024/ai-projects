package com.ghosttalk.server.message;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public final class MessageDtos {

    private MessageDtos() {}

    public record SendMessageRequest(
            @NotBlank String conversationId,
            String content,
            String messageType,
            String clientMessageId,
            String replyToId
    ) {}

    public record ReactionDto(String userId, String emoji) {}

    public record MessageDto(
            String id,
            String conversationId,
            String senderId,
            String content,
            String messageType,
            String replyToId,
            boolean edited,
            boolean deletedForAll,
            String clientMessageId,
            String timestamp,
            List<ReactionDto> reactions
    ) {}

    public record MessagePage(List<MessageDto> messages, boolean hasMore, int page) {}

    public record EditMessageRequest(@NotBlank String content) {}

    public record ReactionRequest(@NotBlank String emoji) {}

    public record TypingRequest(boolean typing) {}

    public record StatusUpdateRequest(@NotBlank String status) {}
}
