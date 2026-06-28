package com.ghosttalk.domain.usecase

import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Chat>> = chatRepository.getChats()
}

class GetMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> =
        chatRepository.getMessages(chatId)
}

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, content: String): Result<Message> =
        chatRepository.sendMessage(chatId, content)
}

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(participantId: String): Result<Chat> =
        chatRepository.createChat(participantId)
}

class MarkChatAsReadUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String) = chatRepository.markAsRead(chatId)
}

class SetTypingUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, isTyping: Boolean) =
        chatRepository.setTyping(chatId, isTyping)
}

class ObserveTypingUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String) = chatRepository.observeTyping(chatId)
}

class ObserveOnlineStatusUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(userId: String) = chatRepository.observeOnlineStatus(userId)
}
