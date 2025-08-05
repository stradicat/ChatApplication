package dev.dmayr.chatapplication.data.network

data class RoomResponse(
    val id: String,
    val name: String,
    val description: String?,
    val lastMessage: String?,
    val lastMessageTime: Long?,
    val participantCount: Int,
    val createdAt: Long,
    val createdBy: String
)
