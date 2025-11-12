package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.User

interface AuthRepository {
    suspend fun signIn(name: String, surname: String, password: String): AuthResult<User>
    suspend fun signUp(name: String, surname: String, phoneNumber: String, email: String, password: String): AuthResult<User>
}