package com.example.bondoman.models

import android.text.TextUtils
import android.util.Patterns

data class Credential (val email: String, val password: String) {
    companion object {
        const val emailDomain = "@std.stei.itb.ac.id"
    }

    fun validateEmail(): Boolean {
        return !TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && email.endsWith(emailDomain)
    }

    fun validatePassword(): Boolean {
        return password.length >= 8
    }
}
