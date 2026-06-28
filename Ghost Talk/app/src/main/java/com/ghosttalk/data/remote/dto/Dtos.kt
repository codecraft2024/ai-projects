package com.ghosttalk.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthRequestDto(
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("otp") val otp: String? = null,
    @SerializedName("nickname") val nickname: String? = null,
    @SerializedName("avatar_id") val avatarId: String? = null,
    @SerializedName("auth_type") val authType: String
)

data class AuthResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserDto
)

data class UserDto(
    @SerializedName("ghost_id") val ghostId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatar_id") val avatarId: String,
    @SerializedName("is_online") val isOnline: Boolean = false,
    @SerializedName("last_seen") val lastSeen: Long? = null
)

data class OtpRequestDto(
    @SerializedName("phone_number") val phoneNumber: String
)

data class OtpResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)

data class MessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("chat_id") val chatId: String,
    @SerializedName("sender_id") val senderId: String,
    @SerializedName("content") val content: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("status") val status: String,
    @SerializedName("is_encrypted") val isEncrypted: Boolean = false
)

data class SendMessageRequestDto(
    @SerializedName("chat_id") val chatId: String,
    @SerializedName("content") val content: String
)

data class ChatDto(
    @SerializedName("id") val id: String,
    @SerializedName("participant") val participant: UserDto,
    @SerializedName("last_message") val lastMessage: MessageDto? = null,
    @SerializedName("unread_count") val unreadCount: Int = 0,
    @SerializedName("updated_at") val updatedAt: Long
)

data class CreateChatRequestDto(
    @SerializedName("participant_id") val participantId: String
)

data class TypingEventDto(
    @SerializedName("chat_id") val chatId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("is_typing") val isTyping: Boolean
)

data class OnlineStatusDto(
    @SerializedName("user_id") val userId: String,
    @SerializedName("is_online") val isOnline: Boolean,
    @SerializedName("last_seen") val lastSeen: Long? = null
)
