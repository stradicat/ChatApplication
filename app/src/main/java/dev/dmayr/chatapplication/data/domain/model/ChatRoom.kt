package dev.dmayr.chatapplication.data.domain.model

data class ChatRoom(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val unreadCount: Int
)
