package com.ghosttalk.presentation.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ghosttalk.R
import com.ghosttalk.databinding.ItemAvatarBinding

class AvatarAdapter(
    private val avatars: List<Pair<String, Int>>,
    private val onAvatarSelected: (String) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    private var selectedId = "ghost_1"

    fun setSelected(avatarId: String) {
        val oldIndex = avatars.indexOfFirst { it.first == selectedId }
        val newIndex = avatars.indexOfFirst { it.first == avatarId }
        selectedId = avatarId
        if (oldIndex >= 0) notifyItemChanged(oldIndex)
        if (newIndex >= 0) notifyItemChanged(newIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bind(avatars[position])
    }

    override fun getItemCount() = avatars.size

    inner class AvatarViewHolder(
        private val binding: ItemAvatarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(avatar: Pair<String, Int>) {
            binding.ivAvatar.setImageResource(avatar.second)
            val isSelected = avatar.first == selectedId
            binding.avatarCard.strokeWidth = if (isSelected) 4 else 0
            binding.avatarCard.strokeColor = ContextCompat.getColor(
                binding.root.context, R.color.ghost_purple
            )
            binding.root.setOnClickListener { onAvatarSelected(avatar.first) }
        }
    }
}
