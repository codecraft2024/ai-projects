package com.ghosttalk.data.remote

import com.ghosttalk.data.remote.dto.AuthRequestDto
import com.ghosttalk.data.remote.dto.AuthResponseDto
import com.ghosttalk.data.remote.dto.ChatDto
import com.ghosttalk.data.remote.dto.CreateChatRequestDto
import com.ghosttalk.data.remote.dto.MessageDto
import com.ghosttalk.data.remote.dto.OnlineStatusDto
import com.ghosttalk.data.remote.dto.OtpRequestDto
import com.ghosttalk.data.remote.dto.OtpResponseDto
import com.ghosttalk.data.remote.dto.SendMessageRequestDto
import com.ghosttalk.data.remote.dto.TypingEventDto
import com.ghosttalk.data.remote.dto.UserDto
import com.ghosttalk.domain.model.AuthType
import kotlinx.coroutines.delay
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Fake backend implementation for development and testing.
 * Simulates network latency and returns mock data.
 */
@Singleton
class FakeBackendService @Inject constructor() {

    private val mockUsers = listOf(
        UserDto("Ghost#4821", "SilentShadow", "ghost_1", true),
        UserDto("Ghost#7392", "MidnightSoul", "ghost_2", true),
        UserDto("Ghost#1056", "PhantomWhisper", "ghost_3", false, System.currentTimeMillis() - 3600000),
        UserDto("Ghost#8843", "ShadowWalker", "ghost_4", true),
        UserDto("Ghost#2917", "EtherealEcho", "ghost_5", false, System.currentTimeMillis() - 7200000),
        UserDto("Ghost#5504", "VoidSeeker", "ghost_6", true),
        UserDto("Ghost#6638", "NightCrawler", "ghost_1", false, System.currentTimeMillis() - 1800000),
        UserDto("Ghost#1192", "SpectralMind", "ghost_2", true)
    )

    private val messages = mutableMapOf<String, MutableList<MessageDto>>()
    private val chats = mutableListOf<ChatDto>()
    private val typingStates = mutableMapOf<String, TypingEventDto>()

    init {
        seedMockChats()
    }

    private fun seedMockChats() {
        mockUsers.take(3).forEachIndexed { index, user ->
            val chatId = "chat_${user.ghostId}"
            val chatMessages = mutableListOf(
                MessageDto(
                    id = UUID.randomUUID().toString(),
                    chatId = chatId,
                    senderId = user.ghostId,
                    content = listOf(
                        "Hey, are you there?",
                        "The shadows are quiet tonight...",
                        "Want to chat anonymously?",
                        "Ghost mode activated 👻"
                    )[index],
                    timestamp = System.currentTimeMillis() - (index + 1) * 3600000L,
                    status = "READ"
                ),
                MessageDto(
                    id = UUID.randomUUID().toString(),
                    chatId = chatId,
                    senderId = "current_user",
                    content = "Yes, I'm here in the shadows.",
                    timestamp = System.currentTimeMillis() - index * 1800000L,
                    status = if (index == 0) "DELIVERED" else "READ"
                )
            )
            messages[chatId] = chatMessages
            chats.add(
                ChatDto(
                    id = chatId,
                    participant = user,
                    lastMessage = chatMessages.last(),
                    unreadCount = if (index == 0) 1 else 0,
                    updatedAt = chatMessages.last().timestamp
                )
            )
        }
    }

    suspend fun sendOtp(request: OtpRequestDto): Response<OtpResponseDto> {
        delay(800)
        return Response.success(
            OtpResponseDto(success = true, message = "OTP sent. Use 123456 for testing.")
        )
    }

    suspend fun login(request: AuthRequestDto): Response<AuthResponseDto> {
        delay(1000)
        val user = when (request.authType) {
            AuthType.MOBILE.name -> {
                if (request.otp != "123456") {
                    return Response.success(
                        AuthResponseDto("", UserDto("", "", ""))
                    ).let { throw IllegalArgumentException("Invalid OTP") }
                }
                UserDto(
                    ghostId = "Ghost#${Random.nextInt(1000, 9999)}",
                    nickname = "Ghost${request.phoneNumber?.takeLast(4)}",
                    avatarId = "ghost_${Random.nextInt(1, 7)}",
                    isOnline = true
                )
            }
            AuthType.ANONYMOUS.name -> UserDto(
                ghostId = "Ghost#${Random.nextInt(1000, 9999)}",
                nickname = request.nickname ?: "Anonymous",
                avatarId = request.avatarId ?: "ghost_1",
                isOnline = true
            )
            else -> throw IllegalArgumentException("Unknown auth type")
        }
        return Response.success(
            AuthResponseDto(token = "fake_token_${UUID.randomUUID()}", user = user)
        )
    }

    suspend fun getChats(): Response<List<ChatDto>> {
        delay(500)
        return Response.success(chats.sortedByDescending { it.updatedAt })
    }

    suspend fun createChat(request: CreateChatRequestDto): Response<ChatDto> {
        delay(600)
        val participant = mockUsers.find { it.ghostId == request.participantId }
            ?: return Response.success(null).let { throw IllegalArgumentException("User not found") }
        val existing = chats.find { it.participant.ghostId == request.participantId }
        if (existing != null) return Response.success(existing)

        val chatId = "chat_${participant.ghostId}"
        val chat = ChatDto(
            id = chatId,
            participant = participant,
            lastMessage = null,
            unreadCount = 0,
            updatedAt = System.currentTimeMillis()
        )
        chats.add(chat)
        messages[chatId] = mutableListOf()
        return Response.success(chat)
    }

    suspend fun getMessages(chatId: String): Response<List<MessageDto>> {
        delay(400)
        return Response.success(messages[chatId] ?: emptyList())
    }

    suspend fun sendMessage(request: SendMessageRequestDto): Response<MessageDto> {
        delay(300)
        val message = MessageDto(
            id = UUID.randomUUID().toString(),
            chatId = request.chatId,
            senderId = "current_user",
            content = request.content,
            timestamp = System.currentTimeMillis(),
            status = "SENT"
        )
        messages.getOrPut(request.chatId) { mutableListOf() }.add(message)
        chats.find { it.id == request.chatId }?.let { chat ->
            val index = chats.indexOf(chat)
            chats[index] = chat.copy(
                lastMessage = message,
                updatedAt = message.timestamp
            )
        }
        return Response.success(message)
    }

    suspend fun markAsRead(chatId: String): Response<Unit> {
        delay(200)
        chats.find { it.id == chatId }?.let { chat ->
            val index = chats.indexOf(chat)
            chats[index] = chat.copy(unreadCount = 0)
        }
        return Response.success(Unit)
    }

    suspend fun setTyping(chatId: String, event: TypingEventDto): Response<Unit> {
        delay(100)
        typingStates[chatId] = event
        return Response.success(Unit)
    }

    fun getTypingState(chatId: String): TypingEventDto? = typingStates[chatId]

    suspend fun discoverUsers(query: String): Response<List<UserDto>> {
        delay(500)
        val filtered = if (query.isBlank()) mockUsers
        else mockUsers.filter {
            it.nickname.contains(query, ignoreCase = true) ||
                it.ghostId.contains(query, ignoreCase = true)
        }
        return Response.success(filtered)
    }

    suspend fun getUser(userId: String): Response<UserDto> {
        delay(300)
        val user = mockUsers.find { it.ghostId == userId }
            ?: return Response.success(null).let { throw IllegalArgumentException("User not found") }
        return Response.success(user)
    }

    suspend fun getOnlineStatus(userId: String): Response<OnlineStatusDto> {
        delay(200)
        val user = mockUsers.find { it.ghostId == userId }
        return Response.success(
            OnlineStatusDto(
                userId = userId,
                isOnline = user?.isOnline ?: false,
                lastSeen = user?.lastSeen
            )
        )
    }
}
