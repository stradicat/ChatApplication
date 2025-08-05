package dev.dmayr.chatapplication.data.network

data class MessageResponse(
    val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: Long,
    val roomId: String,
    val isEncrypted: Boolean = false,
    val messageType: String = "text" // text, image, file, etc.
)
