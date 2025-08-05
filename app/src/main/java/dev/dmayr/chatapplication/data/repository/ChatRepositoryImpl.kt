package dev.dmayr.chatapplication.data.repository

import com.google.gson.Gson
import dev.dmayr.chatapplication.data.database.MessageEntity
import dev.dmayr.chatapplication.data.datasource.local.LocalDataSource
import dev.dmayr.chatapplication.data.datasource.remote.RemoteDataSource
import dev.dmayr.chatapplication.data.datasource.websocket.WebSocketClient
import dev.dmayr.chatapplication.domain.model.ChatRoom
import dev.dmayr.chatapplication.domain.model.Message
import dev.dmayr.chatapplication.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val webSocketClient: WebSocketClient,
    private val gson: Gson
) : ChatRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // Start WebSocket connection
        webSocketClient.connect()

        // Observe incoming messages without making function suspend
        webSocketClient.messages
            .onEach { messageJson ->
                try {
                    val message = gson.fromJson(messageJson, Message::class.java)
                    localDataSource.saveMessage(message.toEntity())
                } catch (e: Exception) {
                    // Handle parsing errors
                    e.printStackTrace()
                }
            }
            .launchIn(repositoryScope) // Launch in repository scope
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

    override fun getChatRooms(): Flow<List<ChatRoom>> = flow {
        try {
            // Try to get from remote first
            val result = remoteDataSource.getChatRooms()
            if (result.isSuccess) {
                val rooms = result.getOrNull()?.map { roomResponse ->
                    ChatRoom(
                        id = roomResponse.id,
                        name = roomResponse.name,
                        lastMessage = roomResponse.lastMessage,
                        lastMessageTime = roomResponse.lastMessageTime,
                        participantCount = roomResponse.participantCount
                    )
                } ?: getMockRooms()
                emit(rooms)
            } else {
                emit(getMockRooms())
            }
        } catch (e: Exception) {
            // Fallback to mock data on network error
            emit(getMockRooms())
        }
    }

    private fun getMockRooms() = listOf(
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

    override suspend fun joinRoom(roomId: String) {
        if (!webSocketClient.connectionStatus.value) {
            webSocketClient.connect()
        }
    }

    override suspend fun leaveRoom(roomId: String) {
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
