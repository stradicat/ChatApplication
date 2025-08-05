package dev.dmayr.chatapplication.presentation.ui.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.presentation.adapter.ChatMessagesAdapter
import dev.dmayr.chatapplication.presentation.ui.viewmodel.ChatUiState
import dev.dmayr.chatapplication.presentation.ui.viewmodel.ChatViewModel

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatMessagesAdapter: ChatMessagesAdapter

    // Dummy current user ID for MVP. In a real app, this would come from authentication.
    private val currentUserId = "user123" // Replace with actual authenticated user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatRoomId = intent.getStringExtra("CHAT_ROOM_ID") ?: "default_room"
        val chatRoomName = intent.getStringExtra("CHAT_ROOM_NAME") ?: "Chat"
        supportActionBar?.title = chatRoomName // Set toolbar title

        setupRecyclerView()
        observeViewModel(chatRoomId)
        setupMessageInput(chatRoomId)
    }

    private fun setupRecyclerView() {
        chatMessagesAdapter = ChatMessagesAdapter(currentUserId)
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true // Show latest messages at the bottom
            }
            adapter = chatMessagesAdapter
        }
    }

    private fun observeViewModel(chatRoomId: String) {
        chatViewModel.loadMessages(chatRoomId) // Load messages for the specific chat room

        chatViewModel.messages.observe(this) { messages ->
            chatMessagesAdapter.submitList(messages)
            binding.chatRecyclerView.scrollToPosition(messages.size - 1) // Scroll to latest message
        }

        chatViewModel.uiState.observe(this) { state ->
            // Handle UI state changes (e.g., show loading spinner, error message)
            when (state) {
                is ChatUiState.Loading -> { /* Show loading indicator */
                }

                is ChatUiState.Success -> { /* Hide loading indicator */
                }

                is ChatUiState.Error -> {
                    // Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    /* Show error message */
                }
            }
        }
    }

    private fun setupMessageInput(chatRoomId: String) {
        binding.sendMessageButton.setOnClickListener {
            val messageContent = binding.messageInputEditText.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                // For MVP, receiverId can be the chatRoomId or a specific user in a 1-1 chat
                chatViewModel.sendMessage(currentUserId, chatRoomId, messageContent)
                binding.messageInputEditText.text.clear() // Clear input field
            }
        }
    }
}
