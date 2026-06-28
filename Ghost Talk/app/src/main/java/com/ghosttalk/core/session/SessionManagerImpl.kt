package com.ghosttalk.core.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ghosttalk.domain.model.AuthType
import com.ghosttalk.domain.model.GhostUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "ghost_talk_session"
)

@Singleton
class SessionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionManager {

    private val dataStore = context.sessionDataStore

    override val currentUser: Flow<GhostUser?> = dataStore.data.map { prefs ->
        val userId = prefs[KEY_USER_ID] ?: return@map null
        GhostUser(
            ghostId = userId,
            nickname = prefs[KEY_NICKNAME] ?: "",
            avatarResId = prefs[KEY_AVATAR_RES] ?: "ghost_1",
            isOnline = true,
            accountCreatedAt = prefs[KEY_ACCOUNT_CREATED]
        )
    }

    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ACCESS_TOKEN] != null && prefs[KEY_USER_ID] != null
    }

    override val hasCompletedOnboarding: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_COMPLETED] ?: false
    }

    override suspend fun saveSession(
        user: GhostUser,
        authType: AuthType,
        accessToken: String,
        refreshToken: String
    ) {
        dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = user.ghostId
            prefs[KEY_NICKNAME] = user.nickname
            prefs[KEY_AVATAR_RES] = user.avatarResId
            prefs[KEY_AUTH_TYPE] = authType.name
            prefs[KEY_ACCESS_TOKEN] = accessToken
            prefs[KEY_REFRESH_TOKEN] = refreshToken
            user.accountCreatedAt?.let { prefs[KEY_ACCOUNT_CREATED] = it }
        }
    }

    override suspend fun updateTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = accessToken
            prefs[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_ONBOARDING_COMPLETED] = completed }
    }

    override suspend fun getAccessToken(): String? = dataStore.data.first()[KEY_ACCESS_TOKEN]
    override suspend fun getRefreshToken(): String? = dataStore.data.first()[KEY_REFRESH_TOKEN]

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_NICKNAME = stringPreferencesKey("nickname")
        private val KEY_AVATAR_RES = stringPreferencesKey("avatar_res")
        private val KEY_AUTH_TYPE = stringPreferencesKey("auth_type")
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val KEY_ACCOUNT_CREATED = stringPreferencesKey("account_created_at")
    }
}
