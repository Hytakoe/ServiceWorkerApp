package com.example.mobileapp.data.model

import com.google.gson.annotations.SerializedName

data class Task (
    val id: Int,
    val carName: String,
    val job: String,
    val comment: String? = null,
){
    // Дополнительные поля для Supabase
    val issueDate: String? = null
    val cost: Int = 0
    val status: String = "В работе"
}
