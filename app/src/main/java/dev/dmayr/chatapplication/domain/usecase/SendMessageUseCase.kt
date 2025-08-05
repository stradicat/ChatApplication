package dev.dmayr.chatapplication.domain.usecase

import dev.dmayr.chatapplication.domain.model.Message
import dev.dmayr.chatapplication.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(message: Message) {
        repository.sendMessage(message)
    }
}
