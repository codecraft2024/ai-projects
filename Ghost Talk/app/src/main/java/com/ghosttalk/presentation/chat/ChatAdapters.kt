package com.ghosttalk.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ghosttalk.R
import com.ghosttalk.core.utils.AvatarProvider
import com.ghosttalk.core.utils.DateTimeUtils
import com.ghosttalk.databinding.ItemChatBinding
import com.ghosttalk.databinding.ItemMessageReceivedBinding
import com.ghosttalk.databinding.ItemMessageSentBinding
import com.ghosttalk.databinding.ItemUserDiscoveryBinding
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.model.Message
import com.ghosttalk.domain.model.MessageStatus

class ChatListAdapter(
    private val onChatClick: (Chat) -> Unit
) : ListAdapter<Chat, ChatListAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatViewHolder(
        private val binding: ItemChatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.tvNickname.text = chat.participant.nickname
            binding.tvGhostId.text = chat.participant.ghostId
            binding.ivAvatar.setImageResource(
                AvatarProvider.getDrawableRes(chat.participant.avatarResId)
            )
            binding.tvLastMessage.text = chat.lastMessage?.content ?: ""
            binding.tvTime.text = chat.lastMessage?.let {
                DateTimeUtils.formatMessageTime(it.timestamp)
            } ?: ""
            binding.tvUnreadCount.isVisible = chat.unreadCount > 0
            binding.tvUnreadCount.text = chat.unreadCount.toString()
            binding.onlineIndicator.isVisible = chat.participant.isOnline
            binding.root.setOnClickListener { onChatClick(chat) }
        }
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Chat, newItem: Chat) = oldItem == newItem
}

class MessageAdapter(
    private val currentUserId: String
) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == currentUserId) VIEW_TYPE_SENT
        else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val binding = ItemMessageSentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SentViewHolder(binding)
            }
            else -> {
                val binding = ItemMessageReceivedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ReceivedViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is SentViewHolder -> holder.bind(message)
            is ReceivedViewHolder -> holder.bind(message)
        }
    }

    inner class SentViewHolder(
        private val binding: ItemMessageSentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.content
            binding.tvTime.text = DateTimeUtils.formatMessageTime(message.timestamp)
            binding.ivStatus.setImageResource(getStatusIcon(message.status))
        }
    }

    inner class ReceivedViewHolder(
        private val binding: ItemMessageReceivedBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.content
            binding.tvTime.text = DateTimeUtils.formatMessageTime(message.timestamp)
        }
    }

    private fun getStatusIcon(status: MessageStatus): Int = when (status) {
        MessageStatus.SENDING -> R.drawable.ic_status_sending
        MessageStatus.SENT -> R.drawable.ic_status_sent
        MessageStatus.DELIVERED -> R.drawable.ic_status_delivered
        MessageStatus.READ -> R.drawable.ic_status_read
        MessageStatus.FAILED -> R.drawable.ic_status_failed
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
}

class UserDiscoveryAdapter(
    private val onUserClick: (GhostUser) -> Unit
) : ListAdapter<GhostUser, UserDiscoveryAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserDiscoveryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(
        private val binding: ItemUserDiscoveryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: GhostUser) {
            binding.tvNickname.text = user.nickname
            binding.tvGhostId.text = user.ghostId
            binding.ivAvatar.setImageResource(AvatarProvider.getDrawableRes(user.avatarResId))
            binding.onlineIndicator.isVisible = user.isOnline
            binding.tvStatus.text = if (user.isOnline) "Online" else "Offline"
            binding.root.setOnClickListener { onUserClick(user) }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<GhostUser>() {
    override fun areItemsTheSame(oldItem: GhostUser, newItem: GhostUser) =
        oldItem.ghostId == newItem.ghostId
    override fun areContentsTheSame(oldItem: GhostUser, newItem: GhostUser) = oldItem == newItem
}
