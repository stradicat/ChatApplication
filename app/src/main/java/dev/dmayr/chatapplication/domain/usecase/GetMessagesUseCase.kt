package dev.dmayr.chatapplication.domain.usecase

import dev.dmayr.chatapplication.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(roomId: String) = repository.getMessages(roomId)
}
