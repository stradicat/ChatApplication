package dev.dmayr.chatapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: Long,
    val roomId: String,
    val isEncrypted: Boolean = false
)
