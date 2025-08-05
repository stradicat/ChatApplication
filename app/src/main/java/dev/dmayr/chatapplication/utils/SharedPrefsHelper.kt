package dev.dmayr.chatapplication.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsHelper @Inject constructor(
    private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("chat_app_prefs", Context.MODE_PRIVATE)
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString("user_id", userId).apply()
    }

    fun getUserId(): String? = prefs.getString("user_id", null)

    fun saveUserName(userName: String) {
        prefs.edit().putString("user_name", userName).apply()
    }

    fun getUserName(): String? = prefs.getString("user_name", null)

    fun saveEncryptionKey(key: String) {
        prefs.edit().putString("encryption_key", key).apply()
    }

    fun getEncryptionKey(): String? = prefs.getString("encryption_key", null)
}
