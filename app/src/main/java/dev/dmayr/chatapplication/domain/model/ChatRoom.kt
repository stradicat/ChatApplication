package dev.dmayr.chatapplication.domain.model

data class ChatRoom(
    val id: String,
    val name: String,
    val lastMessage: String? = null,
    val lastMessageTime: Long? = null,
    val participantCount: Int = 0
)
