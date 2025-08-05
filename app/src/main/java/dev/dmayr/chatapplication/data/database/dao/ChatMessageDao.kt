package dev.dmayr.chatapplication.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.dmayr.chatapplication.data.database.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMessages(messages: List<ChatMessageEntity>)

    @Query("SELECT * FROM chat_messages WHERE sender_id = :userId OR receiver_id = :userId ORDER BY timestamp ASC")
    fun getMessagesForUser(userId: String): Flow<List<ChatMessageEntity>>

    @Query("UPDATE chat_messages SET is_read = 1 WHERE messageId = :messageId")
    suspend fun markMessageAsRead(messageId: Long)

    @Delete
    suspend fun deleteMessage(message: ChatMessageEntity)
}
