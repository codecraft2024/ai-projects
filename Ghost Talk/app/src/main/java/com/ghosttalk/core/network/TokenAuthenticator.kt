package com.ghosttalk.core.network

import com.ghosttalk.data.remote.api.AuthApi
import com.ghosttalk.data.remote.dto.RefreshTokenRequestDto
import com.ghosttalk.core.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val sessionManager: SessionManager,
    private val authApiProvider: Provider<AuthApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null
        val refreshToken = runBlocking { sessionManager.getRefreshToken() } ?: return null

        return runBlocking {
            try {
                val apiResponse = authApiProvider.get().refresh(
                    RefreshTokenRequestDto(refreshToken)
                )
                if (!apiResponse.isSuccessful || apiResponse.body()?.data == null) return@runBlocking null
                val data = apiResponse.body()!!.data!!
                sessionManager.updateTokens(data.accessToken, data.refreshToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${data.accessToken}")
                    .build()
            } catch (_: Exception) {
                sessionManager.clearSession()
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
