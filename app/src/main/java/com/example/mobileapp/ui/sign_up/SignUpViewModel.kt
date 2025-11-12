package com.example.mobileapp.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.domain.usercase.SignInUseCase
import com.example.mobileapp.domain.usercase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(private val signUpUseCase: SignUpUseCase) : ViewModel() {
    private val _uiState = MutableLiveData(SignUpUiState())

    val uiState: LiveData<SignUpUiState> = _uiState

    data class SignUpUiState(
        val name: String = "",
        val surname: String = "",
        val email: String = "",
        val phoneNumber: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isSignUpSuccess: Boolean = false
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

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value?.copy(
            email = email,
            errorMessage = null  // Очищаем ошибку при изменении
        )
    }
    fun onPhoneNumberChanged(phoneNumber: String) {
        _uiState.value = _uiState.value?.copy(
            phoneNumber = phoneNumber,
            errorMessage = null  // Очищаем ошибку при изменении
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value?.copy(
            password = password,
            errorMessage = null  // Очищаем ошибку при изменении
        )
    }

    fun onSignUpClicked() {
        // Устанавливаем состояние загрузки
        _uiState.value = _uiState.value?.copy(
            isLoading = true,
            errorMessage = null,
            isSignUpSuccess = false
        )

        viewModelScope.launch {
            val result = signUpUseCase(
                _uiState.value?.name ?: "",
                _uiState.value?.surname ?: "",
                _uiState.value?.phoneNumber ?: "",
                _uiState.value?.email ?: "",
                _uiState.value?.password ?: ""
            )

            // Обновляем состояние на основе результата
            _uiState.value = when (result) {
                is AuthResult.Success -> {
                    _uiState.value?.copy(
                        isLoading = false,
                        isSignUpSuccess = true
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
        _uiState.value = _uiState.value?.copy(isSignUpSuccess = false)
    }
}