package dev.dmayr.chatapplication.data.repository

import dev.dmayr.chatapplication.data.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: ChatMessage)
    fun getChatMessages(userId: String): Flow<List<ChatMessage>>
    // suspend fun markMessageAsRead(messageId: Long) // ejemplo
}
