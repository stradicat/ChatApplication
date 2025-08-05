package dev.dmayr.chatapplication.data.repository

import com.google.gson.Gson
import dev.dmayr.chatapplication.data.database.dao.ChatMessageDao
import dev.dmayr.chatapplication.data.database.entity.ChatMessageEntity
import dev.dmayr.chatapplication.data.datasource.websocket.WebSocketDataSource
import dev.dmayr.chatapplication.data.datasource.websocket.WebSocketEvent
import dev.dmayr.chatapplication.domain.model.ChatMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
    private val chatMessageDao: ChatMessageDao,
    private val gson: Gson
) : ChatRepository {

    init {
        webSocketDataSource.connect()

        CoroutineScope(Dispatchers.IO).launch {
            webSocketDataSource.events
                .catch { e ->
                    println("Error observing WebSocket events: ${e.localizedMessage}")
                }
                .collect { event ->
                    when (event) {
                        is WebSocketEvent.OnMessage -> {
                            try {
                                val message = gson.fromJson(event.text, ChatMessage::class.java)
                                chatMessageDao.insertMessage(message.toEntity())
                            } catch (e: Exception) {
                                println("Error parsing WebSocket message: ${e.localizedMessage}")
                            }
                        }

                        is WebSocketEvent.OnConnectionOpened -> println("WebSocket Connection Opened")
                        is WebSocketEvent.OnConnectionClosed -> println("WebSocket Connection Closed")
                        is WebSocketEvent.OnConnectionFailed -> println("WebSocket Connection Failed: ${event.throwable.localizedMessage}")
                        is WebSocketEvent.OnConnectionClosing -> println("WebSocket Connection Closing...")
                    }
                }
        }
    }

    override suspend fun sendMessage(message: ChatMessage) {
        val messageJson = gson.toJson(message)
        webSocketDataSource.sendMessage(messageJson)
        chatMessageDao.insertMessage(message.toEntity())
    }

    override fun getChatMessages(userId: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getMessagesForUser(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    private fun ChatMessage.toEntity(): ChatMessageEntity {
        return ChatMessageEntity(
            messageId = this.id,
            senderId = this.senderId,
            receiverId = this.receiverId,
            content = this.content,
            timestamp = this.timestamp,
            isRead = this.isRead
        )
    }

    private fun ChatMessageEntity.toDomainModel(): ChatMessage {
        return ChatMessage(
            id = this.messageId,
            senderId = this.senderId,
            receiverId = this.receiverId,
            content = this.content,
            timestamp = this.timestamp,
            isRead = this.isRead
        )
    }
}
