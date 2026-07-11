package com.ghosttalk.core.network

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object NetworkErrorMapper {

    fun toUserMessage(throwable: Throwable?): String {
        if (throwable == null) return "Something went wrong. Please try again."
        val root = throwable.cause ?: throwable
        return when (root) {
            is UnknownHostException -> "No internet connection. Check your network and try again."
            is ConnectException -> "Cannot reach Ghost Talk server. It may be offline or unreachable."
            is SocketTimeoutException -> "Connection timed out. Please try again."
            is SSLException -> "Secure connection failed. Check the server URL and try again."
            is IOException -> "Network error. Please check your connection and try again."
            else -> root.message?.takeIf { it.isNotBlank() }
                ?: "Something went wrong. Please try again."
        }
    }

    suspend fun <T> runSafely(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
