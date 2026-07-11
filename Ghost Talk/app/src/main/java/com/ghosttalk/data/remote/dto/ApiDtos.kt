package com.ghosttalk.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?,
    @SerializedName("timestamp") val timestamp: String?
)

data class RegisterRequestDto(
    @SerializedName("username") val username: String,
    @SerializedName("mobile") val mobile: String? = null,
    @SerializedName("pin") val pin: String,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("avatarId") val avatarId: String,
    @SerializedName("fingerprintHash") val fingerprintHash: String,
    @SerializedName("deviceModel") val deviceModel: String,
    @SerializedName("manufacturer") val manufacturer: String,
    @SerializedName("osVersion") val osVersion: String,
    @SerializedName("appVersion") val appVersion: String
)

data class LoginPinRequestDto(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("fingerprintHash") val fingerprintHash: String,
    @SerializedName("deviceModel") val deviceModel: String,
    @SerializedName("manufacturer") val manufacturer: String,
    @SerializedName("osVersion") val osVersion: String,
    @SerializedName("appVersion") val appVersion: String
)

data class LoginRequestDto(
    @SerializedName("fingerprintHash") val fingerprintHash: String,
    @SerializedName("username") val username: String? = null,
    @SerializedName("deviceModel") val deviceModel: String,
    @SerializedName("manufacturer") val manufacturer: String,
    @SerializedName("osVersion") val osVersion: String,
    @SerializedName("appVersion") val appVersion: String
)

data class DeviceAccountsRequestDto(
    @SerializedName("fingerprintHash") val fingerprintHash: String
)

data class DeviceAccountsResponseDto(
    @SerializedName("accounts") val accounts: List<SavedAccountDto>,
    @SerializedName("maxAccounts") val maxAccounts: Int,
    @SerializedName("remainingSlots") val remainingSlots: Int
)

data class SavedAccountDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("mobile") val mobile: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("avatarId") val avatarId: String,
    @SerializedName("lastSeen") val lastSeen: String?
)

data class UsernameCheckResponseDto(
    @SerializedName("available") val available: Boolean
)

data class RefreshTokenRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)

data class AuthResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("expiresIn") val expiresIn: Long,
    @SerializedName("user") val user: UserDto
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("mobile") val mobile: String? = null,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("avatarId") val avatarId: String,
    @SerializedName("accountCreatedAt") val accountCreatedAt: String?,
    @SerializedName("online") val online: Boolean = false,
    @SerializedName("lastSeen") val lastSeen: String? = null,
    @SerializedName("verified") val verified: Boolean = false
)

data class UpdateProfileRequestDto(
    @SerializedName("username") val username: String?,
    @SerializedName("mobile") val mobile: String? = null,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("avatarId") val avatarId: String?
)

data class ConversationSummaryDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String?,
    @SerializedName("participant") val participant: UserDto?,
    @SerializedName("lastMessage") val lastMessage: MessagePreviewDto?,
    @SerializedName("unreadCount") val unreadCount: Int,
    @SerializedName("pinned") val pinned: Boolean,
    @SerializedName("muted") val muted: Boolean,
    @SerializedName("archived") val archived: Boolean,
    @SerializedName("updatedAt") val updatedAt: String
)

data class MessagePreviewDto(
    @SerializedName("id") val id: String,
    @SerializedName("content") val content: String?,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("senderUsername") val senderUsername: String?,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("messageType") val messageType: String
)

data class CreateDirectRequestDto(
    @SerializedName("participantId") val participantId: String
)

data class SendMessageRequestDto(
    @SerializedName("conversationId") val conversationId: String,
    @SerializedName("content") val content: String,
    @SerializedName("messageType") val messageType: String = "TEXT",
    @SerializedName("clientMessageId") val clientMessageId: String? = null,
    @SerializedName("replyToId") val replyToId: String? = null
)

data class MessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("conversationId") val conversationId: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("senderUsername") val senderUsername: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("messageType") val messageType: String,
    @SerializedName("replyToId") val replyToId: String?,
    @SerializedName("edited") val edited: Boolean,
    @SerializedName("deletedForAll") val deletedForAll: Boolean,
    @SerializedName("clientMessageId") val clientMessageId: String?,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("reactions") val reactions: List<ReactionDto>?
)

data class ReactionDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("emoji") val emoji: String
)

data class MessagePageDto(
    @SerializedName("messages") val messages: List<MessageDto>,
    @SerializedName("hasMore") val hasMore: Boolean,
    @SerializedName("page") val page: Int
)

data class TypingRequestDto(
    @SerializedName("typing") val typing: Boolean
)

data class ArchiveRequestDto(@SerializedName("archived") val archived: Boolean)
data class PinRequestDto(@SerializedName("pinned") val pinned: Boolean)
data class MuteRequestDto(@SerializedName("muted") val muted: Boolean)
