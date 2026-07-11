package com.ghosttalk.server.message;

import com.ghosttalk.server.common.BusinessException;
import com.ghosttalk.server.common.ResourceNotFoundException;
import com.ghosttalk.server.conversation.Conversation;
import com.ghosttalk.server.conversation.ConversationMember;
import com.ghosttalk.server.conversation.ConversationMemberRepository;
import com.ghosttalk.server.conversation.ConversationRepository;
import com.ghosttalk.server.user.User;
import com.ghosttalk.server.user.UserRepository;
import com.ghosttalk.server.websocket.WebSocketEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageStatusRepository statusRepository;
    private final MessageReactionRepository reactionRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final WebSocketEventPublisher eventPublisher;

    public MessageService(MessageRepository messageRepository,
                          MessageStatusRepository statusRepository,
                          MessageReactionRepository reactionRepository,
                          ConversationRepository conversationRepository,
                          ConversationMemberRepository memberRepository,
                          UserRepository userRepository,
                          WebSocketEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.statusRepository = statusRepository;
        this.reactionRepository = reactionRepository;
        this.conversationRepository = conversationRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public MessageDtos.MessagePage getMessages(UUID userId, UUID conversationId, int page, int size) {
        verifyMembership(conversationId, userId);
        Page<Message> messages = messageRepository.findByConversationId(
                conversationId, PageRequest.of(page, size));
        List<MessageDtos.MessageDto> dtos = messages.getContent().stream()
                .map(this::toDto)
                .toList();
        return new MessageDtos.MessagePage(dtos, messages.hasNext(), page);
    }

    @Transactional
    public MessageDtos.MessageDto sendMessage(UUID userId, MessageDtos.SendMessageRequest request) {
        UUID conversationId = UUID.fromString(request.conversationId());
        ConversationMember senderMember = verifyMembership(conversationId, userId);
        User sender = senderMember.getUser();
        Conversation conversation = senderMember.getConversation();

        if (request.clientMessageId() != null) {
            var existing = messageRepository.findByClientMessageId(request.clientMessageId());
            if (existing.isPresent()) {
                return toDto(existing.get());
            }
        }

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.content());
        message.setMessageType(request.messageType() != null ? request.messageType() : "TEXT");
        message.setClientMessageId(request.clientMessageId());
        if (request.replyToId() != null) {
            message.setReplyToId(UUID.fromString(request.replyToId()));
        }
        message = messageRepository.save(message);

        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        List<ConversationMember> members = memberRepository.findByConversationId(conversationId);
        for (ConversationMember member : members) {
            if (!member.getUser().getId().equals(userId)) {
                member.setUnreadCount(member.getUnreadCount() + 1);
                memberRepository.save(member);
                MessageStatusEntity status = new MessageStatusEntity();
                status.setMessage(message);
                status.setUser(member.getUser());
                status.setStatus("DELIVERED");
                statusRepository.save(status);
            }
        }

        MessageDtos.MessageDto dto = toDto(message);
        eventPublisher.publishMessage(conversationId, dto);
        return dto;
    }

    @Transactional
    public void updateMessageStatus(UUID userId, UUID messageId, String status) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        verifyMembership(message.getConversation().getId(), userId);

        MessageStatusEntity entity = statusRepository.findByMessageIdAndUserId(messageId, userId)
                .orElseGet(() -> {
                    MessageStatusEntity s = new MessageStatusEntity();
                    s.setMessage(message);
                    s.setUser(userRepository.findById(userId).orElseThrow());
                    return s;
                });
        entity.setStatus(status);
        entity.setStatusAt(Instant.now());
        statusRepository.save(entity);

        if ("READ".equals(status)) {
            eventPublisher.publishReadReceipt(message.getConversation().getId(), messageId, userId);
        }
    }

    @Transactional
    public MessageDtos.MessageDto editMessage(UUID userId, UUID messageId, String content) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        if (!message.getSender().getId().equals(userId)) {
            throw new BusinessException("Cannot edit another user's message", 403);
        }
        message.setContent(content);
        message.setEdited(true);
        message.setUpdatedAt(Instant.now());
        MessageDtos.MessageDto dto = toDto(messageRepository.save(message));
        eventPublisher.publishMessageEdit(message.getConversation().getId(), dto);
        return dto;
    }

    @Transactional
    public void deleteForEveryone(UUID userId, UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        if (!message.getSender().getId().equals(userId)) {
            throw new BusinessException("Cannot delete another user's message", 403);
        }
        message.setDeletedForAll(true);
        message.setDeletedAt(Instant.now());
        message.setContent(null);
        messageRepository.save(message);
        eventPublisher.publishMessageDelete(message.getConversation().getId(), messageId);
    }

    @Transactional
    public void addReaction(UUID userId, UUID messageId, String emoji) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        verifyMembership(message.getConversation().getId(), userId);
        User user = userRepository.findById(userId).orElseThrow();
        MessageReaction reaction = new MessageReaction();
        reaction.setMessage(message);
        reaction.setUser(user);
        reaction.setEmoji(emoji);
        reactionRepository.save(reaction);
        eventPublisher.publishReaction(message.getConversation().getId(), messageId, emoji, userId);
    }

    @Transactional
    public void setTyping(UUID userId, UUID conversationId, boolean typing) {
        verifyMembership(conversationId, userId);
        eventPublisher.publishTyping(conversationId, userId, typing);
    }

    private ConversationMember verifyMembership(UUID conversationId, UUID userId) {
        return memberRepository.findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new BusinessException("Not a member", 403));
    }

    private MessageDtos.MessageDto toDto(Message message) {
        List<MessageDtos.ReactionDto> reactions = reactionRepository.findByMessageId(message.getId())
                .stream()
                .map(r -> new MessageDtos.ReactionDto(r.getUser().getId().toString(), r.getEmoji()))
                .toList();

        return new MessageDtos.MessageDto(
                message.getId().toString(),
                message.getConversation().getId().toString(),
                message.getSender().getId().toString(),
                message.getSender().getUsername(),
                message.getContent(),
                message.getMessageType(),
                message.getReplyToId() != null ? message.getReplyToId().toString() : null,
                message.isEdited(),
                message.isDeletedForAll(),
                message.getClientMessageId(),
                message.getCreatedAt().toString(),
                reactions
        );
    }
}
