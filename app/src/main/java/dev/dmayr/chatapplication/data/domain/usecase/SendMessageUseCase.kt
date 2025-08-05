package dev.dmayr.chatapplication.data.domain.usecase

import dev.dmayr.chatapplication.data.repository.ChatRepository
import dev.dmayr.chatapplication.domain.model.ChatMessage
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: ChatMessage) {
        chatRepository.sendMessage(message)
    }
}
