package dev.dmayr.chatapplication.data.repository

import com.google.gson.Gson
import dev.dmayr.chatapplication.data.database.MessageEntity
import dev.dmayr.chatapplication.data.datasource.local.LocalDataSource
import dev.dmayr.chatapplication.data.datasource.websocket.WebSocketClient
import dev.dmayr.chatapplication.domain.model.ChatRoom
import dev.dmayr.chatapplication.domain.model.Message
import dev.dmayr.chatapplication.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val webSocketClient: WebSocketClient,
    private val gson: Gson
) : ChatRepository {

    init {
        // Start WebSocket connection
        webSocketClient.connect()

        // Listen for incoming messages and save them
        observeIncomingMessages()
    }

    private fun observeIncomingMessages() {
        // Note: For echo.websocket.org, you'll receive your own messages back
        // In a real implementation, you'd filter out your own messages
        webSocketClient.messages.collect { messageJson ->
            try {
                val message = gson.fromJson(messageJson, Message::class.java)
                localDataSource.saveMessage(message.toEntity())
            } catch (e: Exception) {
                // Handle parsing errors
            }
        }
    }

    override suspend fun sendMessage(message: Message) {
        // Send to WebSocket (will echo back from echo.websocket.org)
        val messageJson = gson.toJson(message)
        webSocketClient.sendMessage(messageJson)

        // Save to local database immediately for better UX
        localDataSource.saveMessage(message.toEntity())
    }

    override fun getMessages(roomId: String): Flow<List<Message>> =
        localDataSource.getMessages(roomId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getChatRooms(): Flow<List<ChatRoom>> {
        // For MVP with echo.websocket.org, return mock data
        return flowOf(
            listOf(
                ChatRoom(
                    "echo",
                    "Echo Test Room",
                    "Test your messages here",
                    System.currentTimeMillis(),
                    1
                ),
                ChatRoom(
                    "general",
                    "General Chat",
                    "General discussion",
                    System.currentTimeMillis() - 3600000,
                    1
                )
            )
        )
    }

    override suspend fun joinRoom(roomId: String) {
        // For echo.websocket.org, no room concept exists
        // Just connect to the WebSocket
        if (!webSocketClient.connectionStatus.value) {
            webSocketClient.connect()
        }
    }

    override suspend fun leaveRoom(roomId: String) {
        // For echo.websocket.org, just disconnect
        webSocketClient.disconnect()
    }

    override fun getConnectionStatus(): Flow<Boolean> =
        webSocketClient.connectionStatus

    private fun Message.toEntity() = MessageEntity(
        id = id,
        senderId = senderId,
        senderName = senderName,
        content = content,
        timestamp = timestamp,
        roomId = roomId,
        isEncrypted = isEncrypted
    )

    private fun MessageEntity.toDomain() = Message(
        id = id,
        senderId = senderId,
        senderName = senderName,
        content = content,
        timestamp = timestamp,
        roomId = roomId,
        isEncrypted = isEncrypted
    )
}
