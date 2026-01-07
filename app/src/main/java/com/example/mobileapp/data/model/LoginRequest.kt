package com.example.mobileapp.data.model

// Модель для запроса аутентификации
data class LoginRequest(
    val name: String,
    val surname: String,
    val password: String // Пароль будем хранить в отдельной таблице
)
