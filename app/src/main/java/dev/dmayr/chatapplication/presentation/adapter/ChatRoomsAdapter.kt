package dev.dmayr.chatapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.dmayr.chatapplication.R
import dev.dmayr.chatapplication.data.domain.model.ChatRoom

class ChatRoomsAdapter(private val onItemClick: (ChatRoom) -> Unit) :
    RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomViewHolder>() {

    private val chatRooms = mutableListOf<ChatRoom>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRooms[position]
        holder.bind(chatRoom)
        holder.itemView.setOnClickListener { onItemClick(chatRoom) } // [60, 61]
    }

    override fun getItemCount(): Int = chatRooms.size

    fun submitList(newChatRooms: List<ChatRoom>) {
        chatRooms.clear()
        chatRooms.addAll(newChatRooms)
        notifyDataSetChanged()
    }

    class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomNameTextView: TextView = itemView.findViewById(R.id.room_name_text_view)
        private val lastMessageTextView: TextView =
            itemView.findViewById(R.id.last_message_text_view)
        private val unreadCountTextView: TextView =
            itemView.findViewById(R.id.unread_count_text_view)

        fun bind(chatRoom: ChatRoom) {
            roomNameTextView.text = chatRoom.name
            lastMessageTextView.text = chatRoom.lastMessage
            if (chatRoom.unreadCount > 0) {
                unreadCountTextView.text = chatRoom.unreadCount.toString()
                unreadCountTextView.visibility = View.VISIBLE
            } else {
                unreadCountTextView.visibility = View.GONE
            }
        }
    }
}
