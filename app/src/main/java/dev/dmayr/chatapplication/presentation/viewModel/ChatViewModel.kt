package dev.dmayr.chatapplication.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dmayr.chatapplication.domain.model.Message
import dev.dmayr.chatapplication.domain.repository.ChatRepository
import dev.dmayr.chatapplication.domain.usecase.GetMessagesUseCase
import dev.dmayr.chatapplication.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _connectionStatus = MutableStateFlow(false)
    val connectionStatus = _connectionStatus.asStateFlow()

    private var currentRoomId: String? = null
    private var currentUserId: String = UUID.randomUUID().toString()
    private var currentUserName: String = "User${(1000..9999).random()}"

    init {
        observeConnectionStatus()
    }

    fun joinRoom(roomId: String) {
        currentRoomId = roomId
        viewModelScope.launch {
            chatRepository.joinRoom(roomId)
            getMessagesUseCase(roomId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(content: String) {
        currentRoomId?.let { roomId ->
            val message = Message(
                id = UUID.randomUUID().toString(),
                senderId = currentUserId,
                senderName = currentUserName,
                content = content,
                timestamp = System.currentTimeMillis(),
                roomId = roomId
            )

            viewModelScope.launch {
                sendMessageUseCase(message)
            }
        }
    }

    private fun observeConnectionStatus() {
        viewModelScope.launch {
            chatRepository.getConnectionStatus().collect {
                _connectionStatus.value = it
            }
        }
    }

    fun setUserInfo(userId: String, userName: String) {
        currentUserId = userId
        currentUserName = userName
    }
}
