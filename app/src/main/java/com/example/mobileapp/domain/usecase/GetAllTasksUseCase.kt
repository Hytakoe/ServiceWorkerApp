package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.TaskResult
import com.example.mobileapp.data.model.WorkListResult
import com.example.mobileapp.data.repository.TaskRepository

class GetAllTasksUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(): TaskResult<List<Task>> {
        return taskRepository.getAllTasks()
    }
}