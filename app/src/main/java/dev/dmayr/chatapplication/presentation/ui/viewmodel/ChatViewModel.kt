package dev.dmayr.chatapplication.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dmayr.chatapplication.data.domain.model.ChatMessage
import dev.dmayr.chatapplication.data.domain.usecase.GetChatMessagesUseCase
import dev.dmayr.chatapplication.data.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase
) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    // Represents the current UI state (Loading, Success, Error)
    private val _uiState = MutableLiveData<ChatUiState>()
    val uiState: LiveData<ChatUiState> = _uiState

    fun loadMessages(chatRoomId: String) {
        _uiState.value = ChatUiState.Loading // Indicate loading state
        viewModelScope.launch {
            getChatMessagesUseCase(chatRoomId)
                .catch { e ->
                    // Handle errors during message loading
                    _uiState.value =
                        ChatUiState.Error("Failed to load messages: ${e.localizedMessage}")
                }
                .collect { messageList ->
                    _messages.value = messageList // Update LiveData with new messages
                    _uiState.value = ChatUiState.Success(messageList.isEmpty()) // Indicate success
                }
        }
    }

    fun sendMessage(senderId: String, receiverId: String, content: String) {
        viewModelScope.launch {
            val message = ChatMessage(
                senderId = senderId,
                receiverId = receiverId,
                content = content,
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            sendMessageUseCase(message) // Invoke the use case to send the message
            // UI will automatically update via LiveData observing Room changes
        }
    }
}

// Sealed class to represent different states of the chat UI
sealed class ChatUiState {
    object Loading : ChatUiState()
    data class Success(val isEmpty: Boolean) : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}
