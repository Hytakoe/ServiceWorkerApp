package com.example.mobileapp.data.model

data class Task (
    val id: Int,
    val carName: String,
    val job: String,
    val comment: String? = null
)