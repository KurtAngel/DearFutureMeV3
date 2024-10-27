package com.example.dearfutureme.API

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "auth_token"
    }

    // Save token
    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    // Get token
    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    // Clear token
    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}
