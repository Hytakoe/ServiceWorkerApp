package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.User

class AuthRepositoryImpl : AuthRepository {
    override suspend fun signIn(name: String, surname: String, password: String): AuthResult<User> {
        return try {
            if (name == "Ратмир" && surname == "Селютин" && password == "77777") {
                AuthResult.Success(User(1, password, "Test User"))
            } else {
                AuthResult.Error("Invalid credentials")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun signUp(name: String, surname: String, phoneNumber: String, email: String, password: String): AuthResult<User> {
        return try {
            when {
                email.isEmpty() -> AuthResult.Error("Адрес электронной почты необходим")
                !email.contains("@") -> AuthResult.Error("Напишите корректный адрес электронной почты")
                password.length < 5 -> AuthResult.Error("Пароль должен состоять из как минимум 5 символов")
                else -> AuthResult.Success(User(2, email, name))
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Registration failed")
        }
    }

    /*private fun isValidUser(user: User): Boolean {
        return user.email.isNotEmpty() &&
                user.password.length >= 6 &&
                user.email.contains("@")
    }*/
}