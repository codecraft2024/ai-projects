package com.ghosttalk.core.auth

object IdentifierUtils {

    private val phonePattern = Regex("^[+]?[0-9\\s-]{10,15}$")
    private val usernamePattern = Regex("^[a-zA-Z0-9_]{3,20}$")

    enum class IdentifierType { PHONE, USERNAME, UNKNOWN }

    fun detectType(value: String): IdentifierType {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return IdentifierType.UNKNOWN
        val digitRatio = trimmed.count { it.isDigit() }.toFloat() / trimmed.length
        return when {
            digitRatio >= 0.7f && trimmed.filter { it.isDigit() }.length >= 10 -> IdentifierType.PHONE
            usernamePattern.matches(trimmed) -> IdentifierType.USERNAME
            phonePattern.matches(trimmed) -> IdentifierType.PHONE
            else -> IdentifierType.UNKNOWN
        }
    }

    fun normalizePhone(value: String): String = value.filter { it.isDigit() || it == '+' }

    fun isValidUsername(value: String): Boolean = usernamePattern.matches(value.trim())

    fun isValidPhone(value: String): Boolean {
        val digits = value.filter { it.isDigit() }
        return digits.length in 10..15
    }
}
