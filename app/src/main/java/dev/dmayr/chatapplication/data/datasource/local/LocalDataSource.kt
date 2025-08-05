package dev.dmayr.chatapplication.data.datasource.local

import dev.dmayr.chatapplication.data.database.MessageDao
import dev.dmayr.chatapplication.data.database.MessageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val messageDao: MessageDao
) {
    fun getMessages(roomId: String): Flow<List<MessageEntity>> =
        messageDao.getMessagesByRoom(roomId)

    suspend fun saveMessage(message: MessageEntity) =
        messageDao.insertMessage(message)
}
