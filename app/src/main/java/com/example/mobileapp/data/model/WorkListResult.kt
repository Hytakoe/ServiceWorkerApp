package com.example.mobileapp.data.model

sealed class WorkListResult<out T> {
    data class Success<T>(val data: T): WorkListResult<T>()
    data class Error(val message: String) : WorkListResult<Nothing>()
    object Loading: WorkListResult<Nothing>()
}