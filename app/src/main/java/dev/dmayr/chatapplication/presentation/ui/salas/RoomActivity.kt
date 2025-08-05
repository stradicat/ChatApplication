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
class RoomActivity : AppCompatActivity() { // Renamed from SalasActivity

    private lateinit var binding: ActivityRoomBinding // Updated binding type
    private val chatRoomsViewModel: RoomViewModel by viewModels()
    private lateinit var chatRoomsAdapter: ChatRoomsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater) // Updated binding inflation
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        // For MVP, you might load dummy chat rooms or fetch from a simple API
        chatRoomsViewModel.loadChatRooms()
    }

    private fun setupRecyclerView() {
        chatRoomsAdapter = ChatRoomsAdapter { chatRoom ->
            // Handle chat room click, navigate to ChatActivity
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("CHAT_ROOM_ID", chatRoom.id)
                putExtra("CHAT_ROOM_NAME", chatRoom.name)
            }
            startActivity(intent)
        }
        binding.chatRoomsRecyclerView.apply { // This refers to the RecyclerView in activity_room.xml
            layoutManager = LinearLayoutManager(this@RoomActivity) // Updated context
            adapter = chatRoomsAdapter
        }
    }

    private fun observeViewModel() {
        chatRoomsViewModel.chatRooms.observe(this) { rooms ->
            chatRoomsAdapter.submitList(rooms)
        }
        // Observe UI state for loading/error feedback if implemented in ChatRoomsViewModel
    }
}
