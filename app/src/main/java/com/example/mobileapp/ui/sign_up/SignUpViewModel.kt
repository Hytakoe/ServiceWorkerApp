// ui/sign_up/SignUpViewModel.kt
package com.example.mobileapp.ui.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.User
import com.example.mobileapp.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    suspend fun signUp(
        name: String,
        surname: String,
        phoneNumber: String,
        email: String,
        password: String
    ): AuthResult<User> {
        _loading.value = true

        return try {
            signUpUseCase(name, surname, phoneNumber, email, password)
        } finally {
            _loading.value = false
        }
    }
}