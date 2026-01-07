package com.example.mobileapp.data.model

import com.google.gson.annotations.SerializedName

// Модель для создания пользователя
data class WorkerCredentials(
    @SerializedName("worker_id") // Здесь worker_id
    val workerId: Int,

    @SerializedName("password_hash")
    val passwordHash: String
)