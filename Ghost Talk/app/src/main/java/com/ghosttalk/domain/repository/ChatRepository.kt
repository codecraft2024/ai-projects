package com.ghosttalk.domain.repository

import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.OnlineStatus
import com.ghosttalk.domain.model.TypingState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    fun getMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, content: String): Result<Message>
    suspend fun createChat(participantId: String): Result<Chat>
    suspend fun markAsRead(chatId: String)
    fun observeTyping(chatId: String): Flow<TypingState?>
    suspend fun setTyping(chatId: String, isTyping: Boolean)
    fun observeOnlineStatus(userId: String): Flow<OnlineStatus>
}

interface UserDiscoveryRepository {
    fun discoverUsers(query: String = ""): Flow<List<GhostUser>>
    suspend fun getUserById(userId: String): GhostUser?
}
