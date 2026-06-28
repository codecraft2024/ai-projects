package com.ghosttalk.data.remote.api

import com.ghosttalk.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<ApiResponse<AuthResponseDto>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<ApiResponse<AuthResponseDto>>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequestDto): Response<ApiResponse<AuthResponseDto>>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Void>>

    @GET("auth/me")
    suspend fun getProfile(): Response<ApiResponse<UserDto>>

    @PATCH("auth/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): Response<ApiResponse<UserDto>>
}

interface ConversationApi {
    @GET("conversations")
    suspend fun getConversations(): Response<ApiResponse<List<ConversationSummaryDto>>>

    @POST("conversations/direct")
    suspend fun createDirect(@Body request: CreateDirectRequestDto): Response<ApiResponse<ConversationSummaryDto>>

    @POST("conversations/{id}/read")
    suspend fun markAsRead(@Path("id") conversationId: String): Response<ApiResponse<Void>>

    @PATCH("conversations/{id}/archive")
    suspend fun archive(@Path("id") conversationId: String, @Body request: ArchiveRequestDto): Response<ApiResponse<Void>>

    @PATCH("conversations/{id}/pin")
    suspend fun pin(@Path("id") conversationId: String, @Body request: PinRequestDto): Response<ApiResponse<Void>>

    @PATCH("conversations/{id}/mute")
    suspend fun mute(@Path("id") conversationId: String, @Body request: MuteRequestDto): Response<ApiResponse<Void>>

    @GET("conversations/users/search")
    suspend fun searchUsers(@Query("q") query: String): Response<ApiResponse<List<UserDto>>>
}

interface MessageApi {
    @GET("messages/conversation/{conversationId}")
    suspend fun getMessages(
        @Path("conversationId") conversationId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): Response<ApiResponse<MessagePageDto>>

    @POST("messages")
    suspend fun sendMessage(@Body request: SendMessageRequestDto): Response<ApiResponse<MessageDto>>

    @POST("messages/conversation/{conversationId}/typing")
    suspend fun setTyping(
        @Path("conversationId") conversationId: String,
        @Body request: TypingRequestDto
    ): Response<ApiResponse<Void>>

    @POST("messages/{id}/status")
    suspend fun updateStatus(
        @Path("id") messageId: String,
        @Body status: Map<String, String>
    ): Response<ApiResponse<Void>>
}
