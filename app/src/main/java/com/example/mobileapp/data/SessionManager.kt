// data/SessionManager.kt
package com.example.mobileapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.mobileapp.data.model.User

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_SURNAME = "user_surname"
        private const val KEY_USER_TITLE = "user_title"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_USER_ID, user.id)
        editor.putString(KEY_USER_NAME, user.name)
        editor.putString(KEY_USER_SURNAME, user.surname)
        editor.putString(KEY_USER_TITLE, user.title)
        user.phoneNumber?.let { editor.putString(KEY_USER_PHONE, it) }
        user.email?.let { editor.putString(KEY_USER_EMAIL, it) }
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getCurrentUser(): User? {
        if (!isLoggedIn()) return null

        return User(
            id = sharedPreferences.getInt(KEY_USER_ID, -1),
            name = sharedPreferences.getString(KEY_USER_NAME, "") ?: "",
            surname = sharedPreferences.getString(KEY_USER_SURNAME, "") ?: "",
            title = sharedPreferences.getString(KEY_USER_TITLE, "") ?: "",
            phoneNumber = sharedPreferences.getString(KEY_USER_PHONE, null),
            email = sharedPreferences.getString(KEY_USER_EMAIL, null)
        )
    }

    fun getCurrentUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, -1)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}