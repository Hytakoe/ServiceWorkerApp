package com.example.mobileapp.data.model

sealed class TaskResult<out T> {
    data class Success<T>(val data: T): TaskResult<T>()
    data class Error(val message: String) : TaskResult<Nothing>()
    object Loading: TaskResult<Nothing>()
}