package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.TaskInWorkList
import com.example.mobileapp.data.model.TaskResult
import com.example.mobileapp.data.model.WorkListResult

class WorkListRepositoryImpl(
    private val taskRepository: TaskRepository
): WorkListRepository {
    private val workListItems = mutableMapOf<Int, Int>()

    override suspend fun addToWorkList(userId: Int, taskId: Int): WorkListResult<Unit> {
        return try {
            val taskResult = taskRepository.getTaskById(taskId)
            if (taskResult is TaskResult.Success<*>) {
                val currentQuantity = workListItems[taskId] ?: 0
                workListItems[taskId] = currentQuantity + 1
                WorkListResult.Success(Unit)
            } else {
                WorkListResult.Error("task with id=${taskId}")
            }

        } catch (e: Exception) {
            WorkListResult.Error("Failed adding to WorkList: ${e.message}")
        }
    }

    override suspend fun removeFromWorkList(userId: Int, taskId: Int): WorkListResult<Unit> {
        return try {
            val currentQuantity = workListItems[taskId] ?: 0
            if (currentQuantity <= 1) {
                workListItems.remove(taskId)
            } else {
                workListItems[taskId] = currentQuantity - 1
            }
            WorkListResult.Success(Unit)
        } catch (e: Exception) {
            WorkListResult.Error("Failed removing from WorkList: ${e.message}")
        }
    }

    override suspend fun getWorkListItems(userId: Int): WorkListResult<List<TaskInWorkList>> {
        return try {
            val items = workListItems.mapNotNull { (taskId, quantity) ->
                // получаем актуальную информацию о товаре
                when (val result = taskRepository.getTaskById(taskId)) {
                    is TaskResult.Success<*> -> {
                        val task = result.data
                        TaskInWorkList(
                            userId = userId,
                            taskId = taskId
                        )
                    }
                    else -> null
                }
            }
            WorkListResult.Success(items)
        } catch (e: Exception) {
            WorkListResult.Error("Failed getting WorkList items: ${e.message}")
        }
    }

    override suspend fun clearWorkList(userId: Int): WorkListResult<Unit> {
        return try {
            workListItems.clear()
            WorkListResult.Success(Unit)
        } catch (e: Exception) {
            WorkListResult.Error("Failed clearing WorkList: ${e.message}")
        }
    }
}