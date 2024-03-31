package com.example.bondoman.services

import android.content.Context
import android.content.SharedPreferences
import com.example.bondoman.R

class SessionManager (val context: Context) {
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
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