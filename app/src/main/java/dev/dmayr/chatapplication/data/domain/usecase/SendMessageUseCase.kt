package dev.dmayr.chatapplication.data.domain.usecase

import dev.dmayr.chatapplication.data.domain.model.ChatMessage
import dev.dmayr.chatapplication.data.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: ChatMessage) {
        chatRepository.sendMessage(message)
    }
}
