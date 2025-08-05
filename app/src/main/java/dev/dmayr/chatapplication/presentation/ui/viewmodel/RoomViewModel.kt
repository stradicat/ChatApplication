package dev.dmayr.chatapplication.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dmayr.chatapplication.data.domain.model.ChatRoom
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    // In a real app, this would inject a use case to get chat rooms
    // private val getChatRoomsUseCase: GetChatRoomsUseCase
) : ViewModel() {

    private val _chatRooms = MutableLiveData<List<ChatRoom>>()
    val chatRooms: LiveData<List<ChatRoom>> = _chatRooms

    fun loadChatRooms() {
        viewModelScope.launch {
            // For MVP, provide dummy data
            val dummyRooms = listOf(
                ChatRoom(
                    id = "room1",
                    name = "General Chat",
                    lastMessage = "Hello everyone!",
                    lastMessageTimestamp = System.currentTimeMillis() - 3600000,
                    unreadCount = 5
                ),
                ChatRoom(
                    id = "room2",
                    name = "Kotlin Devs",
                    lastMessage = "New Android update is out!",
                    lastMessageTimestamp = System.currentTimeMillis() - 1800000,
                    unreadCount = 0
                ),
                ChatRoom(
                    id = "room3",
                    name = "Project Alpha",
                    lastMessage = "Meeting at 3 PM.",
                    lastMessageTimestamp = System.currentTimeMillis() - 600000,
                    unreadCount = 2
                )
            )
            _chatRooms.value = dummyRooms
        }
    }
}
