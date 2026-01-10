package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.TaskResult
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.data.model.supabase.CarModelInfo

interface TaskRepository {
    suspend fun getTasksForWorker(workerId: Int): List<Task>
    suspend fun getTasks(): List<Task> // Старый метод для обратной совместимости
    suspend fun getTaskById(id: Int): Task?
    suspend fun getAllTasks(): List<Task> // Для админа или отладки
    suspend fun createTask(carId: Int, job: String, comment: String?, workerId: Int): Boolean
    suspend fun completeTask(taskId: Int): Boolean
    suspend fun getCarModels(): Map<Int, CarModelInfo>
}