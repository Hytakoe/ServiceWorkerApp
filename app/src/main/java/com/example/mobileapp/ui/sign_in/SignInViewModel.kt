package com.example.mobileapp.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.SessionManager
import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.AuthResult.*
import com.example.mobileapp.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class SignInUiState(
        val name: String = "",
        val surname: String = "",
        val password: String = "",
        val nameError: String? = null,
        val surnameError: String? = null,
        val passwordError: String? = null,
        val isLoading: Boolean = false,
        val isSignInSuccess: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _authState = MutableStateFlow<AuthResult<Unit>?>(null)
    val authState = _authState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onSurameChanged(surname: String) {
        _uiState.value = _uiState.value.copy(surname = surname)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onSignInClicked() {
        val currentState = _uiState.value

        val errors = mutableListOf<String>()

        if (currentState.name.isBlank()) {
            _uiState.value = currentState.copy(nameError = "Введите имя")
            errors.add("Имя обязательно")
        }

        if (currentState.surname.isBlank()) {
            _uiState.value = currentState.copy(surnameError = "Введите фамилию")
            errors.add("Фамилия обязательна")
        }

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(passwordError = "Введите пароль")
            errors.add("Пароль обязателен")
        } else if (currentState.password.length < 5) {
            _uiState.value = currentState.copy(passwordError = "Пароль должен быть минимум 5 символов")
            errors.add("Пароль слишком короткий")
        }

        if (errors.isNotEmpty()) {
            return
        }

        signIn(currentState.name, currentState.surname, currentState.password)
    }

    private fun signIn(name: String, surname: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = signInUseCase(name, surname, password)

            _loading.value = false
            _uiState.value = _uiState.value.copy(isLoading = false)

            when (result) {
                is Success -> {
                    sessionManager.saveUser(result.data)
                    _uiState.value = _uiState.value.copy(isSignInSuccess = true)
                    _authState.value = Success(Unit)
                }
                is Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message,
                        isSignInSuccess = false
                    )
                    _authState.value = Error(result.message)
                }

                Loading -> TODO()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetSuccessState() {
        _uiState.value = _uiState.value.copy(isSignInSuccess = false)
    }

    fun clearAuthState() {
        _authState.value = null
    }
}