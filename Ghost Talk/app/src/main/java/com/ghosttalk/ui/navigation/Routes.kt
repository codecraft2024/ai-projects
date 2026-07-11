package com.ghosttalk.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val ACCOUNT_HUB = "account_hub"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CHAT_DETAIL = "chat/{chatId}/{participantName}/{participantId}"
    const val USER_PROFILE = "user/{userId}/{username}"

    fun chatDetail(chatId: String, participantName: String, participantId: String): String {
        val encodedName = java.net.URLEncoder.encode(participantName, Charsets.UTF_8.name())
        return "chat/$chatId/$encodedName/$participantId"
    }

    fun userProfile(userId: String, username: String): String {
        val encoded = java.net.URLEncoder.encode(username, Charsets.UTF_8.name())
        return "user/$userId/$encoded"
    }
}

enum class HomeTab(val route: String, val label: String) {
    CHATS("home/chats", "Chats"),
    DISCOVER("home/discover", "Discover"),
    PROFILE("home/profile", "Profile"),
    SETTINGS("home/settings", "Settings")
}
