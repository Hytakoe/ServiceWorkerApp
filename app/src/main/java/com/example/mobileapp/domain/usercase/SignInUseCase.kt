package com.example.mobileapp.domain.usercase

import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.User
import com.example.mobileapp.data.repository.AuthRepository

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, surname: String, password: String): AuthResult<User> {
        return authRepository.signIn(name, surname, password)
    }
}