package com.ghosttalk.data.repository

import com.ghosttalk.core.encryption.MessageEncryptionLayer
import com.ghosttalk.data.local.dao.ChatDao
import com.ghosttalk.data.local.dao.MessageDao
import com.ghosttalk.data.local.dao.UserDao
import com.ghosttalk.data.local.entity.ChatEntity
import com.ghosttalk.data.local.entity.MessageEntity
import com.ghosttalk.data.local.entity.UserEntity
import com.ghosttalk.data.local.toDomain
import com.ghosttalk.data.local.toEntity
import com.ghosttalk.data.remote.FakeBackendService
import com.ghosttalk.data.remote.api.ChatApi
import com.ghosttalk.data.remote.dto.MessageDto
import com.ghosttalk.data.remote.dto.SendMessageRequestDto
import com.ghosttalk.data.remote.dto.TypingEventDto
import com.ghosttalk.data.remote.toDomain
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.MessageStatus
import com.ghosttalk.domain.model.OnlineStatus
import com.ghosttalk.domain.model.TypingState
import com.ghosttalk.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val userDao: UserDao,
    private val fakeBackend: FakeBackendService,
    private val encryptionLayer: MessageEncryptionLayer
) : ChatRepository {

    private val typingFlows = mutableMapOf<String, MutableStateFlow<TypingState?>>()

    override fun getChats(): Flow<List<Chat>> = chatDao.getAllChats()
        .map { chatEntities ->
            chatEntities.mapNotNull { entity ->
                val participant = userDao.getUserById(entity.participantId)?.toDomain()
                    ?: return@mapNotNull null
                val lastMessage = entity.lastMessageId?.let { msgId ->
                    messageDao.getMessageById(msgId)?.toDomain()
                }
                entity.toDomain(participant, lastMessage)
            }
        }
        .onStart { syncChatsFromRemote() }

    override fun getMessages(chatId: String): Flow<List<Message>> =
        messageDao.getMessagesForChat(chatId)
            .map { entities -> entities.map { it.toDomain() } }
            .onStart { syncMessagesFromRemote(chatId) }

    override suspend fun sendMessage(chatId: String, content: String): Result<Message> {
        return runCatching {
            val encrypted = encryptionLayer.encryptOutgoingMessage(content, chatId)
            val response = chatApi.sendMessage(
                SendMessageRequestDto(chatId, encrypted.cipherText)
            )
            if (!response.isSuccessful || response.body() == null) {
                throw Exception("Failed to send message")
            }
            val message = response.body()!!.toDomain().copy(isEncrypted = true)
            messageDao.insertMessage(message.toEntity())

            delay(1000)
            val delivered = message.copy(status = MessageStatus.DELIVERED)
            messageDao.updateMessage(delivered.toEntity())
            delivered
        }
    }

    override suspend fun createChat(participantId: String): Result<Chat> = runCatching {
        val response = chatApi.createChat(
            com.ghosttalk.data.remote.dto.CreateChatRequestDto(participantId)
        )
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Failed to create chat")
        }
        val chatDto = response.body()!!
        userDao.insertUser(chatDto.participant.toUserEntity())
        chatDao.insertChat(
            ChatEntity(
                id = chatDto.id,
                participantId = chatDto.participant.ghostId,
                unreadCount = 0,
                updatedAt = chatDto.updatedAt
            )
        )
        chatDto.toDomain()
    }

    override suspend fun markAsRead(chatId: String) {
        chatApi.markAsRead(chatId)
        chatDao.markAsRead(chatId)
    }

    override fun observeTyping(chatId: String): Flow<TypingState?> {
        return typingFlows.getOrPut(chatId) { MutableStateFlow(null) }
    }

    override suspend fun setTyping(chatId: String, isTyping: Boolean) {
        chatApi.setTyping(
            chatId,
            TypingEventDto(chatId, "current_user", isTyping)
        )
        typingFlows.getOrPut(chatId) { MutableStateFlow(null) }.value =
            TypingState(chatId, "current_user", isTyping)
    }

    override fun observeOnlineStatus(userId: String): Flow<OnlineStatus> = kotlinx.coroutines.flow.flow {
        while (true) {
            val response = fakeBackend.getOnlineStatus(userId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                emit(
                    OnlineStatus(
                        userId = dto.userId,
                        isOnline = dto.isOnline,
                        lastSeen = dto.lastSeen
                    )
                )
            }
            delay(5000)
        }
    }

    private suspend fun syncChatsFromRemote() {
        val response = chatApi.getChats()
        if (response.isSuccessful) {
            response.body()?.forEach { chatDto ->
                userDao.insertUser(chatDto.participant.toUserEntity())
                chatDao.insertChat(
                    ChatEntity(
                        id = chatDto.id,
                        participantId = chatDto.participant.ghostId,
                        lastMessageId = chatDto.lastMessage?.id,
                        unreadCount = chatDto.unreadCount,
                        updatedAt = chatDto.updatedAt
                    )
                )
                chatDto.lastMessage?.let { msg ->
                    messageDao.insertMessage(msg.toMessageEntity())
                }
            }
        }
    }

    private suspend fun syncMessagesFromRemote(chatId: String) {
        val response = chatApi.getMessages(chatId)
        if (response.isSuccessful) {
            response.body()?.forEach { messageDao.insertMessage(it.toMessageEntity()) }
        }
    }
}

private fun com.ghosttalk.data.remote.dto.UserDto.toUserEntity() = UserEntity(
    ghostId = ghostId,
    nickname = nickname,
    avatarResId = avatarId,
    isOnline = isOnline,
    lastSeen = lastSeen
)

private fun MessageDto.toMessageEntity() = MessageEntity(
    id = id,
    chatId = chatId,
    senderId = senderId,
    content = content,
    timestamp = timestamp,
    status = status,
    isEncrypted = isEncrypted
)
