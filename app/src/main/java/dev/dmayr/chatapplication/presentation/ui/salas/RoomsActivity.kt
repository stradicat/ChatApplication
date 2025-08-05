package dev.dmayr.chatapplication.presentation.ui.salas

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityRoomsBinding
import dev.dmayr.chatapplication.presentation.adapter.RoomAdapter
import dev.dmayr.chatapplication.presentation.ui.chat.ChatActivity
import dev.dmayr.chatapplication.presentation.viewmodel.RoomsViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomsBinding
    private val viewModel: RoomsViewModel by viewModels()
    private lateinit var roomAdapter: RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        roomAdapter = RoomAdapter { room ->
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("ROOM_ID", room.id)
                putExtra("ROOM_NAME", room.name)
            }
            startActivity(intent)
        }

        binding.recyclerRooms.apply {
            adapter = roomAdapter
            layoutManager = LinearLayoutManager(this@RoomsActivity)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.rooms.collect { rooms ->
                roomAdapter.submitList(rooms)
            }
        }
    }
}
