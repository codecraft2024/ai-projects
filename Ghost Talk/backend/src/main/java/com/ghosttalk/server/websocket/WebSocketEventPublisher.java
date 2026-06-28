package com.ghosttalk.server.websocket;

import com.ghosttalk.server.message.MessageDtos;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WebSocketEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishMessage(UUID conversationId, MessageDtos.MessageDto message) {
        messagingTemplate.convertAndSend("/topic/conversation." + conversationId, Map.of(
                "type", "MESSAGE",
                "data", message
        ));
    }

    public void publishMessageEdit(UUID conversationId, MessageDtos.MessageDto message) {
        messagingTemplate.convertAndSend("/topic/conversation." + conversationId, Map.of(
                "type", "MESSAGE_EDIT",
                "data", message
        ));
    }

    public void publishMessageDelete(UUID conversationId, UUID messageId) {
        messagingTemplate.convertAndSend("/topic/conversation." + conversationId, Map.of(
                "type", "MESSAGE_DELETE",
                "messageId", messageId.toString()
        ));
    }

    public void publishTyping(UUID conversationId, UUID userId, boolean typing) {
        messagingTemplate.convertAndSend("/topic/conversation." + conversationId, Map.of(
                "type", "TYPING",
                "userId", userId.toString(),
                "typing", typing
        ));
    }

    public void publishReadReceipt(UUID conversationId, UUID messageId, UUID userId) {
        messagingTemplate.convertAndSend("/topic/conversation." + conversationId, Map.of(
                "type", "READ_RECEIPT",
                "messageId", messageId.toString(),
                "userId", userId.toString()
        ));
    }

    public void publishReaction(UUID conversationId, UUID messageId, String emoji, UUID userId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "REACTION");
        payload.put("messageId", messageId.toString());
        payload.put("emoji", emoji);
        payload.put("userId", userId.toString());
        messagingTemplate.convertAndSend("/topic/conversation." + conversationId, payload);
    }

    public void publishPresence(UUID userId, boolean online) {
        messagingTemplate.convertAndSend("/topic/presence", Map.of(
                "type", "PRESENCE",
                "userId", userId.toString(),
                "online", online
        ));
    }
}
