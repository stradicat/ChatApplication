package dev.dmayr.chatapplication.domain.usecase

import dev.dmayr.chatapplication.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatRoomsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() = repository.getChatRooms()
}
