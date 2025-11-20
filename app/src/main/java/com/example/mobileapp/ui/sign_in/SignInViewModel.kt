package com.example.mobileapp.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.domain.usecase.SignInUseCase
import kotlinx.coroutines.launch

class SignInViewModel(private val signInUseCase: SignInUseCase) : ViewModel() {
    private val _uiState = MutableLiveData(SignInUiState())
    val uiState: LiveData<SignInUiState> = _uiState

    data class SignInUiState(
        val name: String = "",
        val surname: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isSignInSuccess: Boolean = false
    )

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value?.copy(
            name = name,
            errorMessage = null  // Очищаем ошибку при изменении
        )
    }
    fun onSurameChanged(surname: String) {
        _uiState.value = _uiState.value?.copy(
            surname = surname,
            errorMessage = null  // Очищаем ошибку при изменении
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value?.copy(
            password = password,
            errorMessage = null  // Очищаем ошибку при изменении
        )
    }

    fun onSignInClicked() {
        // Устанавливаем состояние загрузки
        _uiState.value = _uiState.value?.copy(
            isLoading = true,
            errorMessage = null,
            isSignInSuccess = false
        )

        viewModelScope.launch {
            val result = signInUseCase(
                _uiState.value?.name ?: "",
                _uiState.value?.surname ?: "",
                _uiState.value?.password ?: ""
            )

            // Обновляем состояние на основе результата
            _uiState.value = when (result) {
                is AuthResult.Success -> {
                    _uiState.value?.copy(
                        isLoading = false,
                        isSignInSuccess = true
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value?.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                else -> _uiState.value?.copy(isLoading = false)
            }
        }
    }

    // Дополнительные методы для управления состоянием
    fun clearError() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    fun resetSuccessState() {
        _uiState.value = _uiState.value?.copy(isSignInSuccess = false)
    }
}