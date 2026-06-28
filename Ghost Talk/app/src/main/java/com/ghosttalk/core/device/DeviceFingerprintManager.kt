package com.ghosttalk.core.device

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Generates a stable, privacy-respecting device fingerprint for abuse prevention.
 * Combines hardware characteristics with a secure installation ID stored in EncryptedSharedPreferences.
 * Only the SHA-256 hash is transmitted to the backend.
 */
@Singleton
class DeviceFingerprintManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "ghost_device_secure",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getFingerprintHash(): String {
        val installationId = getOrCreateInstallationId()
        val raw = buildString {
            append(installationId)
            append('|')
            append(Build.MANUFACTURER)
            append('|')
            append(Build.MODEL)
            append('|')
            append(Build.VERSION.SDK_INT)
            append('|')
            append(getAppVersion())
            append('|')
            append(Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown")
        }
        return sha256(raw)
    }

    fun getDeviceModel(): String = Build.MODEL
    fun getManufacturer(): String = Build.MANUFACTURER
    fun getOsVersion(): String = Build.VERSION.RELEASE
    fun getAppVersion(): String = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
    } catch (_: PackageManager.NameNotFoundException) {
        "1.0.0"
    }

    private fun getOrCreateInstallationId(): String {
        val key = "installation_id"
        prefs.getString(key, null)?.let { return it }
        val id = UUID.randomUUID().toString()
        prefs.edit().putString(key, id).apply()
        return id
    }

    private fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(input.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }
}
