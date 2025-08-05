package dev.dmayr.chatapplication.presentation.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.dmayr.chatapplication.databinding.ItemRoomBinding
import dev.dmayr.chatapplication.domain.model.ChatRoom

class RoomAdapter(
    private val onRoomClick: (ChatRoom) -> Unit
) : ListAdapter<ChatRoom, RoomAdapter.RoomViewHolder>(RoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = ItemRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoomViewHolder(binding, onRoomClick)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RoomViewHolder(
        private val binding: ItemRoomBinding,
        private val onRoomClick: (ChatRoom) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(room: ChatRoom) {
            binding.apply {
                textRoomName.text = room.name
                textLastMessage.text = room.lastMessage ?: "No messages yet"
                textParticipantCount.text = "${room.participantCount} members"

                root.setOnClickListener { onRoomClick(room) }
            }
        }
    }

    private class RoomDiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean =
            oldItem == newItem
    }
}
