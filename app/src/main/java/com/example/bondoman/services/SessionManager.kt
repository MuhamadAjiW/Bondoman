package com.example.bondoman.services

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.example.bondoman.R

class SessionManager (val context: Context) {
    private val masterKey = MasterKey
        .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPref = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        const val AUTH_TOKEN = "auth_token"
    }

    fun saveToken(token: String) {
        val sharedPrefEditor = sharedPref.edit()
        sharedPrefEditor.putString(AUTH_TOKEN, token)
        sharedPrefEditor.apply()
    }

    fun getToken(): String? {
        return sharedPref.getString(AUTH_TOKEN, null)
    }

    fun clearToken() {
        val sharedPrefEditor = sharedPref.edit()
        sharedPrefEditor.remove(AUTH_TOKEN)
        sharedPrefEditor.apply()
    }
}