package dev.dmayr.chatapplication.data.domain.model

data class ChatMessage(
    val id: Long = 0,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = false
)
