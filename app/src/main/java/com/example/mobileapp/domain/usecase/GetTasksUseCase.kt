package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.repository.TaskRepository

class GetTasksUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): List<Task> {
        return repository.getTasks()
    }
    suspend operator fun invoke(workerId: Int): List<Task> {
        return repository.getTasksForWorker(workerId)
    }
}