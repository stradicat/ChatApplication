package dev.dmayr.chatapplication.presentation.ui.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityChatBinding
import dev.dmayr.chatapplication.presentation.adapter.MessageAdapter
import dev.dmayr.chatapplication.presentation.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        val roomId = intent.getStringExtra("ROOM_ID") ?: "1"
        val roomName = intent.getStringExtra("ROOM_NAME") ?: "Chat"

        supportActionBar?.title = roomName
        viewModel.joinRoom(roomId)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        binding.recyclerMessages.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }
    }

    private fun setupClickListeners() {
        binding.buttonSend.setOnClickListener {
            val message = binding.editMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.editMessage.setText("")
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                messageAdapter.submitList(messages)
                if (messages.isNotEmpty()) {
                    binding.recyclerMessages.scrollToPosition(messages.size - 1)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.connectionStatus.collect { isConnected ->
                binding.textConnectionStatus.text = if (isConnected) "Connected" else "Disconnected"
            }
        }
    }
}
