package dev.dmayr.chatapplication.domain.repository

import dev.dmayr.chatapplication.domain.model.ChatRoom
import dev.dmayr.chatapplication.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: Message)
    fun getMessages(roomId: String): Flow<List<Message>>
    fun getChatRooms(): Flow<List<ChatRoom>>
    suspend fun joinRoom(roomId: String)
    suspend fun leaveRoom(roomId: String)
    fun getConnectionStatus(): Flow<Boolean>
}
