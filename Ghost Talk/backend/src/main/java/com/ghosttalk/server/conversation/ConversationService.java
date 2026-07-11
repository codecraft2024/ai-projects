package com.ghosttalk.server.conversation;

import com.ghosttalk.server.common.BusinessException;
import com.ghosttalk.server.common.ResourceNotFoundException;
import com.ghosttalk.server.message.Message;
import com.ghosttalk.server.message.MessageRepository;
import com.ghosttalk.server.user.User;
import com.ghosttalk.server.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               ConversationMemberRepository memberRepository,
                               UserRepository userRepository,
                               MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public List<ConversationDtos.ConversationSummary> listConversations(UUID userId) {
        return memberRepository.findByUserIdOrderByConversationUpdatedAtDesc(userId).stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public ConversationDtos.ConversationSummary createDirectConversation(UUID userId, UUID participantId) {
        if (userId.equals(participantId)) {
            throw new BusinessException("Cannot create conversation with yourself");
        }
        userRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<ConversationMember> existing = memberRepository.findDirectConversation(userId, participantId);
        if (existing.isPresent()) {
            return toSummary(existing.get());
        }

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User participant = userRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Conversation conversation = new Conversation();
        conversation.setType("DIRECT");
        conversation.setCreatedById(userId);
        conversation = conversationRepository.save(conversation);

        addMember(conversation, creator, "MEMBER");
        addMember(conversation, participant, "MEMBER");

        return toSummary(memberRepository.findByConversationIdAndUserId(conversation.getId(), userId)
                .orElseThrow());
    }

    @Transactional
    public ConversationDtos.ConversationSummary createGroup(UUID userId, String title, List<UUID> memberIds) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Conversation conversation = new Conversation();
        conversation.setType("GROUP");
        conversation.setTitle(title);
        conversation.setCreatedById(userId);
        conversation = conversationRepository.save(conversation);

        addMember(conversation, creator, "ADMIN");
        for (UUID memberId : memberIds) {
            User member = userRepository.findById(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + memberId));
            if (!memberId.equals(userId)) {
                addMember(conversation, member, "MEMBER");
            }
        }
        return toSummary(memberRepository.findByConversationIdAndUserId(conversation.getId(), userId)
                .orElseThrow());
    }

    @Transactional
    public void archiveConversation(UUID userId, UUID conversationId, boolean archived) {
        ConversationMember member = getMember(conversationId, userId);
        Conversation conversation = member.getConversation();
        conversation.setArchived(archived);
        conversationRepository.save(conversation);
    }

    @Transactional
    public void pinConversation(UUID userId, UUID conversationId, boolean pinned) {
        ConversationMember member = getMember(conversationId, userId);
        member.setPinned(pinned);
        memberRepository.save(member);
    }

    @Transactional
    public void muteConversation(UUID userId, UUID conversationId, boolean muted) {
        ConversationMember member = getMember(conversationId, userId);
        member.setMuted(muted);
        memberRepository.save(member);
    }

    @Transactional
    public void markAsRead(UUID userId, UUID conversationId) {
        ConversationMember member = getMember(conversationId, userId);
        member.setUnreadCount(0);
        member.setLastReadAt(Instant.now());
        memberRepository.save(member);
    }

    private void addMember(Conversation conversation, User user, String role) {
        ConversationMember member = new ConversationMember();
        member.setConversation(conversation);
        member.setUser(user);
        member.setRole(role);
        memberRepository.save(member);
    }

    private ConversationMember getMember(UUID conversationId, UUID userId) {
        return memberRepository.findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new BusinessException("Not a member of this conversation", 403));
    }

    private ConversationDtos.ConversationSummary toSummary(ConversationMember member) {
        Conversation conversation = member.getConversation();
        List<ConversationMember> members = memberRepository.findByConversationId(conversation.getId());

        ConversationDtos.UserSummary otherUser = null;
        if ("DIRECT".equals(conversation.getType())) {
            otherUser = members.stream()
                    .filter(m -> !m.getUser().getId().equals(member.getUser().getId()))
                    .findFirst()
                    .map(m -> new ConversationDtos.UserSummary(
                            m.getUser().getId().toString(),
                            m.getUser().getUsername(),
                            m.getUser().getDisplayName(),
                            m.getUser().getBio(),
                            m.getUser().getAvatarId(),
                            m.getUser().isOnline(),
                            m.getUser().getLastSeen() != null ? m.getUser().getLastSeen().toString() : null,
                            false
                    ))
                    .orElse(null);
        }

        Message lastMessage = messageRepository.findByConversationId(conversation.getId(),
                org.springframework.data.domain.PageRequest.of(0, 1)).getContent().stream().findFirst().orElse(null);

        ConversationDtos.MessagePreview preview = null;
        if (lastMessage != null) {
            preview = new ConversationDtos.MessagePreview(
                    lastMessage.getId().toString(),
                    lastMessage.getContent(),
                    lastMessage.getSender().getId().toString(),
                    lastMessage.getSender().getUsername(),
                    lastMessage.getCreatedAt().toString(),
                    lastMessage.getMessageType()
            );
        }

        return new ConversationDtos.ConversationSummary(
                conversation.getId().toString(),
                conversation.getType(),
                conversation.getTitle(),
                otherUser,
                preview,
                member.getUnreadCount(),
                member.isPinned(),
                member.isMuted(),
                conversation.isArchived(),
                conversation.getUpdatedAt().toString()
        );
    }
}
