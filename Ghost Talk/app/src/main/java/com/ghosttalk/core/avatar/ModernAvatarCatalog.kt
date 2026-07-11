package com.ghosttalk.core.avatar

import androidx.compose.ui.graphics.Color

data class ModernAvatar(
    val id: String,
    val emoji: String,
    val background: Color,
    val accent: Color,
    val label: String
)

object ModernAvatarCatalog {
    val all: List<ModernAvatar> = listOf(
        avatar("modern_01", "👻", 0xFF7C3AED, 0xFFDDD6FE, "Ghost Violet"),
        avatar("modern_02", "🦊", 0xFFEA580C, 0xFFFED7AA, "Amber Fox"),
        avatar("modern_03", "🐼", 0xFF1F2937, 0xFFE5E7EB, "Panda"),
        avatar("modern_04", "🦋", 0xFF2563EB, 0xFFBFDBFE, "Blue Wing"),
        avatar("modern_05", "🌸", 0xFFDB2777, 0xFFFBCFE8, "Sakura"),
        avatar("modern_06", "🦁", 0xFFD97706, 0xFFFEF3C7, "Golden Lion"),
        avatar("modern_07", "🐸", 0xFF16A34A, 0xFFBBF7D0, "Forest"),
        avatar("modern_08", "🦄", 0xFF8B5CF6, 0xFFEDE9FE, "Unicorn"),
        avatar("modern_09", "🐙", 0xFF7C3AED, 0xFFC4B5FD, "Deep Sea"),
        avatar("modern_10", "🦉", 0xFF4338CA, 0xFFC7D2FE, "Night Owl"),
        avatar("modern_11", "🐱", 0xFFBE185D, 0xFFFCE7F3, "Kitty"),
        avatar("modern_12", "🐶", 0xFF92400E, 0xFFFDE68A, "Puppy"),
        avatar("modern_13", "🌙", 0xFF312E81, 0xFFE0E7FF, "Moon"),
        avatar("modern_14", "⭐", 0xFFCA8A04, 0xFFFEF9C3, "Star"),
        avatar("modern_15", "🔮", 0xFF6D28D9, 0xFFEDE9FE, "Crystal"),
        avatar("modern_16", "🎭", 0xFF0F766E, 0xFFCCFBF1, "Masque"),
        avatar("modern_17", "🌊", 0xFF0369A1, 0xFFBAE6FD, "Wave"),
        avatar("modern_18", "🔥", 0xFFDC2626, 0xFFFECACA, "Ember"),
        avatar("modern_19", "❄️", 0xFF0284C7, 0xFFE0F2FE, "Frost"),
        avatar("modern_20", "🍀", 0xFF15803D, 0xFFDCFCE7, "Clover"),
        avatar("modern_21", "🎸", 0xFF9F1239, 0xFFFCE7F3, "Rock"),
        avatar("modern_22", "📚", 0xFF4B5563, 0xFFF3F4F6, "Scholar"),
        avatar("modern_23", "🚀", 0xFF1D4ED8, 0xFFDBEAFE, "Rocket"),
        avatar("modern_24", "💎", 0xFF0891B2, 0xFFCFFAFE, "Diamond")
    )

    fun find(id: String): ModernAvatar? = all.find { it.id == id }

    private fun avatar(id: String, emoji: String, bg: Long, accent: Long, label: String) =
        ModernAvatar(id, emoji, Color(bg), Color(accent), label)
}
