package com.ghosttalk.data.remote.api

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @POST("auth/otp/send")
    suspend fun sendOtp(@Body request: OtpRequestDto): Response<OtpResponseDto>

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequestDto): Response<AuthResponseDto>
}

interface ChatApi {
    @GET("chats")
    suspend fun getChats(): Response<List<ChatDto>>

    @POST("chats")
    suspend fun createChat(@Body request: CreateChatRequestDto): Response<ChatDto>

    @GET("chats/{chatId}/messages")
    suspend fun getMessages(@Path("chatId") chatId: String): Response<List<MessageDto>>

    @POST("messages")
    suspend fun sendMessage(@Body request: SendMessageRequestDto): Response<MessageDto>

    @POST("chats/{chatId}/read")
    suspend fun markAsRead(@Path("chatId") chatId: String): Response<Unit>

    @POST("chats/{chatId}/typing")
    suspend fun setTyping(
        @Path("chatId") chatId: String,
        @Body event: TypingEventDto
    ): Response<Unit>
}

interface UserApi {
    @GET("users/discover")
    suspend fun discoverUsers(@Query("q") query: String = ""): Response<List<UserDto>>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserDto>

    @GET("users/{userId}/status")
    suspend fun getOnlineStatus(@Path("userId") userId: String): Response<OnlineStatusDto>
}
