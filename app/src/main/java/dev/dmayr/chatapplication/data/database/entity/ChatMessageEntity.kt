package dev.dmayr.chatapplication.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val messageId: Long = 0,
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "receiver_id") val receiverId: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "is_read") val isRead: Boolean
)
