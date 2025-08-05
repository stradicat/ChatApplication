package dev.dmayr.chatapplication.domain.model

data class ChatRoom(
    val id: String, // Unique chat room identifier
    val name: String,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val unreadCount: Int // Number of unread messages in this room
)
