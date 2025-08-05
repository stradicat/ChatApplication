package dev.dmayr.chatapplication.domain.model

data class Message(
    val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: Long,
    val roomId: String,
    val isEncrypted: Boolean = false
)
