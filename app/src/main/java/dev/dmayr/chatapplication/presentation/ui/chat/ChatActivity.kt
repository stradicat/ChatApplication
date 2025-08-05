package dev.dmayr.chatapplication.presentation.ui.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityChatBinding
import dev.dmayr.chatapplication.presentation.adapter.ChatMessagesAdapter
import dev.dmayr.chatapplication.presentation.ui.viewmodel.ChatUiState
import dev.dmayr.chatapplication.presentation.ui.viewmodel.ChatViewModel

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatMessagesAdapter: ChatMessagesAdapter

    private val currentUserId = "user123" // Reemplazar con el ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatRoomId = intent.getStringExtra("CHAT_ROOM_ID") ?: "default_room"
        val chatRoomName = intent.getStringExtra("CHAT_ROOM_NAME") ?: "Chat"
        supportActionBar?.title = chatRoomName

        setupRecyclerView()
        observeViewModel(chatRoomId)
        setupMessageInput(chatRoomId)
    }

    private fun setupRecyclerView() {
        chatMessagesAdapter = ChatMessagesAdapter(currentUserId)
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = chatMessagesAdapter
        }
    }

    private fun observeViewModel(chatRoomId: String) {
        chatViewModel.loadMessages(chatRoomId)

        chatViewModel.messages.observe(this) { messages ->
            chatMessagesAdapter.submitList(messages)
            binding.chatRecyclerView.scrollToPosition(messages.size - 1)
        }

        chatViewModel.uiState.observe(this) { state ->

            when (state) {
                is ChatUiState.Loading -> {
                }

                is ChatUiState.Success -> {
                }

                is ChatUiState.Error -> {
                    /* error  */
                }
            }
        }
    }

    private fun setupMessageInput(chatRoomId: String) {
        binding.sendMessageButton.setOnClickListener {
            val messageContent = binding.messageInputEditText.text.toString().trim()
            if (messageContent.isNotEmpty()) {

                chatViewModel.sendMessage(currentUserId, chatRoomId, messageContent)
                binding.messageInputEditText.text.clear()
            }
        }
    }
}
