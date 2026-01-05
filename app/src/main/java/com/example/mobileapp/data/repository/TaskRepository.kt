package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.TaskResult
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.supabase.CarModelInfo

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun getTaskById(id: Int): Task?
    suspend fun createTask(carId: Int, job: String, comment: String?): Boolean
    suspend fun completeTask(taskId: Int): Boolean
    suspend fun getCarModels(): Map<Int, CarModelInfo> // Кэш моделей авто
}