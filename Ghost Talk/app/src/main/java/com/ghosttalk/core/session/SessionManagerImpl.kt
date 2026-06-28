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
        val ghostId = prefs[KEY_GHOST_ID] ?: return@map null
        GhostUser(
            ghostId = ghostId,
            nickname = prefs[KEY_NICKNAME] ?: "",
            avatarResId = prefs[KEY_AVATAR_RES] ?: "ghost_1",
            isOnline = true
        )
    }

    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_AUTH_TOKEN] != null && prefs[KEY_GHOST_ID] != null
    }

    override val hasCompletedOnboarding: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_COMPLETED] ?: false
    }

    override suspend fun saveSession(user: GhostUser, authType: AuthType, token: String) {
        dataStore.edit { prefs ->
            prefs[KEY_GHOST_ID] = user.ghostId
            prefs[KEY_NICKNAME] = user.nickname
            prefs[KEY_AVATAR_RES] = user.avatarResId
            prefs[KEY_AUTH_TYPE] = authType.name
            prefs[KEY_AUTH_TOKEN] = token
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = completed
        }
    }

    override suspend fun getAuthToken(): String? =
        dataStore.data.first()[KEY_AUTH_TOKEN]

    companion object {
        private val KEY_GHOST_ID = stringPreferencesKey("ghost_id")
        private val KEY_NICKNAME = stringPreferencesKey("nickname")
        private val KEY_AVATAR_RES = stringPreferencesKey("avatar_res")
        private val KEY_AUTH_TYPE = stringPreferencesKey("auth_type")
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }
}
