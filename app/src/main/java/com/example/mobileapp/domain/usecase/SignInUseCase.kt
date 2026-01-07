// domain/usecase/SignInUseCase.kt
package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.User
import com.example.mobileapp.data.repository.AuthRepository

class SignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, surname: String, password: String): AuthResult<User> {
        return repository.signIn(name, surname, password)
    }
}

