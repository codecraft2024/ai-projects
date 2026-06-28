package com.ghosttalk.core.utils

import com.ghosttalk.R
import kotlin.random.Random

object GhostIdGenerator {
    fun generate(): String = "Ghost#${Random.nextInt(1000, 9999)}"
}

object AvatarProvider {
    private val avatars = listOf(
        "ghost_1" to R.drawable.ic_ghost_1,
        "ghost_2" to R.drawable.ic_ghost_2,
        "ghost_3" to R.drawable.ic_ghost_3,
        "ghost_4" to R.drawable.ic_ghost_4,
        "ghost_5" to R.drawable.ic_ghost_5,
        "ghost_6" to R.drawable.ic_ghost_6
    )

    fun randomAvatarId(): String = avatars.random().first

    fun getDrawableRes(avatarId: String): Int =
        avatars.find { it.first == avatarId }?.second ?: R.drawable.ic_ghost_1

    fun allAvatars(): List<Pair<String, Int>> = avatars
}

object DateTimeUtils {
    fun formatMessageTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < 60_000 -> "Just now"
            diff < 3_600_000 -> "${diff / 60_000}m ago"
            diff < 86_400_000 -> {
                val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                sdf.format(java.util.Date(timestamp))
            }
            else -> {
                val sdf = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
                sdf.format(java.util.Date(timestamp))
            }
        }
    }
}

object NicknameValidator {
    private val pattern = Regex("^[a-zA-Z0-9_]{3,20}$")

    fun isValid(nickname: String): Boolean = pattern.matches(nickname)

    fun getError(nickname: String): String? = when {
        nickname.length < 3 -> "Nickname must be at least 3 characters"
        nickname.length > 20 -> "Nickname must be at most 20 characters"
        !pattern.matches(nickname) -> "Only letters, numbers, and underscores allowed"
        else -> null
    }
}

object PhoneValidator {
    fun isValid(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }
        return digits.length in 10..15
    }
}
