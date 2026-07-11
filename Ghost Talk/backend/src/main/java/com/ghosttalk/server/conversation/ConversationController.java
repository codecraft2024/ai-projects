package com.ghosttalk.server.conversation;

import com.ghosttalk.server.common.ApiResponse;
import com.ghosttalk.server.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final UserRepository userRepository;

    public ConversationController(ConversationService conversationService, UserRepository userRepository) {
        this.conversationService = conversationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ApiResponse<List<ConversationDtos.ConversationSummary>> list(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponse.ok(conversationService.listConversations(userId));
    }

    @PostMapping("/direct")
    public ApiResponse<ConversationDtos.ConversationSummary> createDirect(
            Authentication auth,
            @Valid @RequestBody ConversationDtos.CreateDirectRequest request
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponse.ok(conversationService.createDirectConversation(
                userId, UUID.fromString(request.participantId())));
    }

    @PostMapping("/group")
    public ApiResponse<ConversationDtos.ConversationSummary> createGroup(
            Authentication auth,
            @Valid @RequestBody ConversationDtos.CreateGroupRequest request
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        List<UUID> memberIds = request.memberIds().stream().map(UUID::fromString).toList();
        return ApiResponse.ok(conversationService.createGroup(userId, request.title(), memberIds));
    }

    @PatchMapping("/{id}/archive")
    public ApiResponse<Void> archive(Authentication auth, @PathVariable UUID id,
                                     @RequestBody ConversationDtos.ArchiveRequest request) {
        conversationService.archiveConversation((UUID) auth.getPrincipal(), id, request.archived());
        return ApiResponse.ok(null);
    }

    @PatchMapping("/{id}/pin")
    public ApiResponse<Void> pin(Authentication auth, @PathVariable UUID id,
                                 @RequestBody ConversationDtos.PinRequest request) {
        conversationService.pinConversation((UUID) auth.getPrincipal(), id, request.pinned());
        return ApiResponse.ok(null);
    }

    @PatchMapping("/{id}/mute")
    public ApiResponse<Void> mute(Authentication auth, @PathVariable UUID id,
                                  @RequestBody ConversationDtos.MuteRequest request) {
        conversationService.muteConversation((UUID) auth.getPrincipal(), id, request.muted());
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/read")
    public ApiResponse<Void> markRead(Authentication auth, @PathVariable UUID id) {
        conversationService.markAsRead((UUID) auth.getPrincipal(), id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/users/search")
    public ApiResponse<List<ConversationDtos.UserSummary>> searchUsers(
            @RequestParam(defaultValue = "") String q
    ) {
        var users = userRepository.searchActiveUsers(q).stream()
                .map(u -> new ConversationDtos.UserSummary(
                        u.getId().toString(),
                        u.getUsername(),
                        u.getDisplayName(),
                        u.getBio(),
                        u.getAvatarId(),
                        u.isOnline(),
                        u.getLastSeen() != null ? u.getLastSeen().toString() : null,
                        false))
                .toList();
        return ApiResponse.ok(users);
    }
}
