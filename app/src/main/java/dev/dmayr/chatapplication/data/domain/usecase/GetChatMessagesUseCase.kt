package dev.dmayr.chatapplication.data.domain.usecase

import dev.dmayr.chatapplication.data.domain.model.ChatMessage
import dev.dmayr.chatapplication.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(userId: String): Flow<List<ChatMessage>> {
        return chatRepository.getChatMessages(userId)
    }
}
