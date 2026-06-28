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
import com.ghosttalk.data.remote.api.AuthApi
import com.ghosttalk.data.remote.api.ChatApi
import com.ghosttalk.data.remote.api.UserApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthApi @Inject constructor(
    private val fakeBackend: FakeBackendService
) : AuthApi {
    override suspend fun sendOtp(request: OtpRequestDto): Response<OtpResponseDto> =
        fakeBackend.sendOtp(request)

    override suspend fun login(request: AuthRequestDto): Response<AuthResponseDto> =
        fakeBackend.login(request)
}

@Singleton
class FakeChatApi @Inject constructor(
    private val fakeBackend: FakeBackendService
) : ChatApi {
    override suspend fun getChats(): Response<List<ChatDto>> = fakeBackend.getChats()
    override suspend fun createChat(request: CreateChatRequestDto): Response<ChatDto> =
        fakeBackend.createChat(request)
    override suspend fun getMessages(chatId: String): Response<List<MessageDto>> =
        fakeBackend.getMessages(chatId)
    override suspend fun sendMessage(request: SendMessageRequestDto): Response<MessageDto> =
        fakeBackend.sendMessage(request)
    override suspend fun markAsRead(chatId: String): Response<Unit> =
        fakeBackend.markAsRead(chatId)
    override suspend fun setTyping(chatId: String, event: TypingEventDto): Response<Unit> =
        fakeBackend.setTyping(chatId, event)
}

@Singleton
class FakeUserApi @Inject constructor(
    private val fakeBackend: FakeBackendService
) : UserApi {
    override suspend fun discoverUsers(query: String): Response<List<UserDto>> =
        fakeBackend.discoverUsers(query)
    override suspend fun getUser(userId: String): Response<UserDto> =
        fakeBackend.getUser(userId)
    override suspend fun getOnlineStatus(userId: String): Response<OnlineStatusDto> =
        fakeBackend.getOnlineStatus(userId)
}
