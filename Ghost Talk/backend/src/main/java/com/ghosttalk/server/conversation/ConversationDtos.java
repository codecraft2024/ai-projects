package com.ghosttalk.server.conversation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public final class ConversationDtos {

    private ConversationDtos() {}

    public record CreateDirectRequest(@NotBlank String participantId) {}

    public record CreateGroupRequest(
            @NotBlank String title,
            @NotEmpty List<String> memberIds
    ) {}

    public record UserSummary(
            String id,
            String username,
            String avatarId,
            boolean online,
            String lastSeen
    ) {}

    public record MessagePreview(
            String id,
            String content,
            String senderId,
            String timestamp,
            String messageType
    ) {}

    public record ConversationSummary(
            String id,
            String type,
            String title,
            UserSummary participant,
            MessagePreview lastMessage,
            int unreadCount,
            boolean pinned,
            boolean muted,
            boolean archived,
            String updatedAt
    ) {}

    public record ArchiveRequest(boolean archived) {}
    public record PinRequest(boolean pinned) {}
    public record MuteRequest(boolean muted) {}
}
