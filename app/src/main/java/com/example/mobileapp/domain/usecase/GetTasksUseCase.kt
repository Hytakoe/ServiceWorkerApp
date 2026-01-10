package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.repository.TaskRepository

class GetTasksUseCase(
    private val repository: TaskRepository
) {
    // Старый метод (оставьте для обратной совместимости)
    suspend operator fun invoke(): List<Task> {
        return repository.getTasks()
    }

    // Новый метод с workerId
    suspend operator fun invoke(workerId: Int): List<Task> {
        return repository.getTasksForWorker(workerId)
    }
}