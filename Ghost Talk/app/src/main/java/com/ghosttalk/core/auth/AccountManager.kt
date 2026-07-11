package com.ghosttalk.core.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ghosttalk.domain.model.SavedAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "ghost_accounts_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _accounts = MutableStateFlow(loadAccounts())
    val accounts: StateFlow<List<SavedAccount>> = _accounts.asStateFlow()

    fun getLastActiveAccountId(): String? = prefs.getString(KEY_LAST_ACTIVE, null)

    fun setLastActiveAccountId(userId: String) {
        prefs.edit().putString(KEY_LAST_ACTIVE, userId).apply()
    }

    fun upsertAccount(account: SavedAccount) {
        val updated = loadAccounts()
            .filterNot { it.userId == account.userId }
            .plus(account.copy(lastActiveAt = System.currentTimeMillis()))
            .sortedByDescending { it.lastActiveAt }
            .take(MAX_ACCOUNTS)
        saveAccounts(updated)
        setLastActiveAccountId(account.userId)
    }

    fun removeAccount(userId: String) {
        val updated = loadAccounts().filterNot { it.userId == userId }
        saveAccounts(updated)
        if (getLastActiveAccountId() == userId) {
            prefs.edit().remove(KEY_LAST_ACTIVE).apply()
        }
    }

    fun canAddAccount(): Boolean = loadAccounts().size < MAX_ACCOUNTS

    private fun loadAccounts(): List<SavedAccount> {
        val json = prefs.getString(KEY_ACCOUNTS, "[]") ?: "[]"
        val array = JSONArray(json)
        return buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                add(
                    SavedAccount(
                        userId = obj.getString("userId"),
                        username = obj.getString("username"),
                        mobile = obj.optString("mobile").takeIf { it.isNotBlank() },
                        displayName = obj.getString("displayName"),
                        avatarId = obj.getString("avatarId"),
                        lastActiveAt = obj.optLong("lastActiveAt", 0L)
                    )
                )
            }
        }.sortedByDescending { it.lastActiveAt }
    }

    private fun saveAccounts(accounts: List<SavedAccount>) {
        val array = JSONArray()
        accounts.forEach { account ->
            array.put(
                JSONObject().apply {
                    put("userId", account.userId)
                    put("username", account.username)
                    put("mobile", account.mobile ?: "")
                    put("displayName", account.displayName)
                    put("avatarId", account.avatarId)
                    put("lastActiveAt", account.lastActiveAt)
                }
            )
        }
        prefs.edit().putString(KEY_ACCOUNTS, array.toString()).apply()
        _accounts.value = accounts
    }

    companion object {
        private const val KEY_ACCOUNTS = "saved_accounts"
        private const val KEY_LAST_ACTIVE = "last_active_account"
        const val MAX_ACCOUNTS = 3
    }
}
