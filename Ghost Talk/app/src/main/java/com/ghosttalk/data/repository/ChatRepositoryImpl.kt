package com.ghosttalk.data.repository

import com.ghosttalk.core.encryption.MessageEncryptionLayer
import com.ghosttalk.core.session.SessionManager
import com.ghosttalk.data.local.dao.ChatDao
import com.ghosttalk.data.local.dao.MessageDao
import com.ghosttalk.data.local.dao.PendingMessageDao
import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.entity.ChatEntity
import com.ghosttalk.data.local.entity.MessageEntity
import com.ghosttalk.data.local.entity.PendingMessageEntity
import com.ghosttalk.data.local.toDomain
import com.ghosttalk.data.local.toEntity
import com.ghosttalk.data.remote.api.ConversationApi
import com.ghosttalk.data.remote.api.MessageApi
import com.ghosttalk.data.remote.dto.CreateDirectRequestDto
import com.ghosttalk.data.remote.dto.SendMessageRequestDto
import com.ghosttalk.data.remote.dto.TypingRequestDto
import com.ghosttalk.data.remote.toDomain
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.MessageStatus
import com.ghosttalk.domain.model.OnlineStatus
import com.ghosttalk.domain.model.TypingState
import com.ghosttalk.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val conversationApi: ConversationApi,
    private val messageApi: MessageApi,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val userDao: UserDao,
    private val pendingMessageDao: PendingMessageDao,
    private val sessionManager: SessionManager,
    private val encryptionLayer: MessageEncryptionLayer
) : ChatRepository {

    private val typingFlows = mutableMapOf<String, MutableStateFlow<TypingState?>>()

    override fun getChats(): Flow<List<Chat>> = chatDao.getAllChats()
        .map { entities ->
            val userId = sessionManager.currentUser.first()?.ghostId
            entities.mapNotNull { entity ->
                val participant = userDao.getUserById(entity.participantId)?.toDomain() ?: return@mapNotNull null
                val lastMessage = entity.lastMessageId?.let { messageDao.getMessageById(it)?.toDomain() }
                entity.toDomain(participant, lastMessage)
            }
        }
        .onStart { syncConversations() }

    override fun getMessages(chatId: String): Flow<List<Message>> =
        messageDao.getMessagesForChat(chatId)
            .map { list -> list.map { it.toDomain() } }
            .onStart { syncMessages(chatId) }

    override suspend fun sendMessage(chatId: String, content: String): Result<Message> {
        val clientId = UUID.randomUUID().toString()
        val pending = PendingMessageEntity(
            clientMessageId = clientId,
            chatId = chatId,
            content = content
        )
        pendingMessageDao.insert(pending)

        return try {
            val encrypted = encryptionLayer.encryptOutgoingMessage(content, chatId)
            val response = messageApi.sendMessage(
                SendMessageRequestDto(
                    conversationId = chatId,
                    content = encrypted.cipherText,
                    clientMessageId = clientId
                )
            )
            val body = response.body()
            if (!response.isSuccessful || body?.data == null) {
                throw Exception(body?.message ?: "Failed to send")
            }
            val message = body.data!!.toDomain().copy(status = MessageStatus.SENT, isEncrypted = true)
            messageDao.insertMessage(message.toEntity())
            pendingMessageDao.delete(clientId)
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createChat(participantId: String): Result<Chat> = try {
        val response = conversationApi.createDirect(CreateDirectRequestDto(participantId))
        val body = response.body()
        if (body?.success != true || body.data == null) {
            throw Exception(body?.message ?: "Failed to create chat")
        }
        val userId = sessionManager.currentUser.first()?.ghostId ?: ""
        val chat = body.data!!.toDomain(userId) ?: throw Exception("Invalid conversation")
        cacheConversation(chat)
        Result.success(chat)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun markAsRead(chatId: String) {
        conversationApi.markAsRead(chatId)
        chatDao.markAsRead(chatId)
    }

    override fun observeTyping(chatId: String): Flow<TypingState?> =
        typingFlows.getOrPut(chatId) { MutableStateFlow(null) }

    override suspend fun setTyping(chatId: String, isTyping: Boolean) {
        messageApi.setTyping(chatId, TypingRequestDto(isTyping))
        typingFlows.getOrPut(chatId) { MutableStateFlow(null) }.value =
            TypingState(chatId, sessionManager.currentUser.first()?.ghostId ?: "", isTyping)
    }

    override fun observeOnlineStatus(userId: String): Flow<OnlineStatus> =
        kotlinx.coroutines.flow.flow {
            userDao.getAllUsers().collect { users ->
                val user = users.find { it.ghostId == userId }
                emit(OnlineStatus(userId, user?.isOnline ?: false, user?.lastSeen))
            }
        }

    private suspend fun syncConversations() {
        val response = conversationApi.getConversations()
        val body = response.body() ?: return
        if (!body.success || body.data == null) return
        val userId = sessionManager.currentUser.first()?.ghostId ?: return
        body.data!!.forEach { dto ->
            dto.toDomain(userId)?.let { cacheConversation(it) }
        }
    }

    private suspend fun syncMessages(chatId: String) {
        val response = messageApi.getMessages(chatId)
        val body = response.body() ?: return
        if (!body.success || body.data == null) return
        body.data!!.messages.forEach { messageDao.insertMessage(it.toDomain().toEntity()) }
    }

    private suspend fun cacheConversation(chat: Chat) {
        userDao.insertUser(chat.participant.toEntity())
        chatDao.insertChat(
            ChatEntity(
                id = chat.id,
                participantId = chat.participant.ghostId,
                lastMessageId = chat.lastMessage?.id,
                unreadCount = chat.unreadCount,
                updatedAt = chat.updatedAt,
                isPinned = chat.isPinned,
                isMuted = chat.isMuted,
                isArchived = chat.isArchived,
                type = chat.type
            )
        )
        chat.lastMessage?.let { messageDao.insertMessage(it.toEntity()) }
    }
}
