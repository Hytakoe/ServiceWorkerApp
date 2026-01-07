// domain/usecase/SignUpUseCase.kt
package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.User
import com.example.mobileapp.data.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        surname: String,
        phoneNumber: String,
        email: String,
        password: String
    ): AuthResult<User> {
        return repository.signUp(name, surname, phoneNumber, email, password)
    }
}