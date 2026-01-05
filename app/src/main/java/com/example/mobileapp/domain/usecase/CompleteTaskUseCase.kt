package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.repository.TaskRepository

class CompleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int): Boolean {
        return repository.completeTask(taskId)
    }
}