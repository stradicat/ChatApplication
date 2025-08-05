import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.dmayr.chatapplication.databinding.ActivityRoomBinding
import dev.dmayr.chatapplication.presentation.adapter.ChatRoomsAdapter
import dev.dmayr.chatapplication.presentation.ui.chat.ChatActivity
import dev.dmayr.chatapplication.presentation.ui.viewmodel.RoomViewModel

@AndroidEntryPoint
class RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomBinding
    private val chatRoomsViewModel: RoomViewModel by viewModels()
    private lateinit var chatRoomsAdapter: ChatRoomsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        chatRoomsViewModel.loadChatRooms()
    }

    private fun setupRecyclerView() {
        chatRoomsAdapter = ChatRoomsAdapter { chatRoom ->
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("CHAT_ROOM_ID", chatRoom.id)
                putExtra("CHAT_ROOM_NAME", chatRoom.name)
            }
            startActivity(intent)
        }
        binding.chatRoomsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoomActivity)
            adapter = chatRoomsAdapter
        }
    }

    private fun observeViewModel() {
        chatRoomsViewModel.chatRooms.observe(this) { rooms ->
            chatRoomsAdapter.submitList(rooms)
        }
    }
}
