package com.ghosttalk.server.message;

import com.ghosttalk.server.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/conversation/{conversationId}")
    public ApiResponse<MessageDtos.MessagePage> getMessages(
            Authentication auth,
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.ok(messageService.getMessages((UUID) auth.getPrincipal(), conversationId, page, size));
    }

    @PostMapping
    public ApiResponse<MessageDtos.MessageDto> send(
            Authentication auth,
            @Valid @RequestBody MessageDtos.SendMessageRequest request
    ) {
        return ApiResponse.ok(messageService.sendMessage((UUID) auth.getPrincipal(), request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<MessageDtos.MessageDto> edit(
            Authentication auth,
            @PathVariable UUID id,
            @Valid @RequestBody MessageDtos.EditMessageRequest request
    ) {
        return ApiResponse.ok(messageService.editMessage((UUID) auth.getPrincipal(), id, request.content()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteForEveryone(Authentication auth, @PathVariable UUID id) {
        messageService.deleteForEveryone((UUID) auth.getPrincipal(), id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/reactions")
    public ApiResponse<Void> react(
            Authentication auth,
            @PathVariable UUID id,
            @Valid @RequestBody MessageDtos.ReactionRequest request
    ) {
        messageService.addReaction((UUID) auth.getPrincipal(), id, request.emoji());
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(
            Authentication auth,
            @PathVariable UUID id,
            @Valid @RequestBody MessageDtos.StatusUpdateRequest request
    ) {
        messageService.updateMessageStatus((UUID) auth.getPrincipal(), id, request.status());
        return ApiResponse.ok(null);
    }

    @PostMapping("/conversation/{conversationId}/typing")
    public ApiResponse<Void> typing(
            Authentication auth,
            @PathVariable UUID conversationId,
            @RequestBody MessageDtos.TypingRequest request
    ) {
        messageService.setTyping((UUID) auth.getPrincipal(), conversationId, request.typing());
        return ApiResponse.ok(null);
    }
}
