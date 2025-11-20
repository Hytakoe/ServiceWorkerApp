package com.example.mobileapp.data.repository

import com.example.mobileapp.data.model.TaskInWorkList
import com.example.mobileapp.data.model.WorkListResult

interface WorkListRepository {
    suspend fun addToWorkList(userId: Int, taskId: Int): WorkListResult<Unit>
    suspend fun removeFromWorkList(userId: Int, taskId: Int): WorkListResult<Unit>
    suspend fun getWorkListItems(userId: Int): WorkListResult<List<TaskInWorkList>>
    suspend fun clearWorkList(userId: Int): WorkListResult<Unit>
}