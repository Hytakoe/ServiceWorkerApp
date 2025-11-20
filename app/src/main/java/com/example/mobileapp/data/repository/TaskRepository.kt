package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.TaskResult
import com.example.mobileapp.data.model.Task

interface TaskRepository {
    suspend fun getTaskById(taskId: Int): TaskResult<Task>
    suspend fun getAllTasks(): TaskResult<List<Task>>
}