package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.TaskInWorkList
import com.example.mobileapp.data.model.WorkListResult
import com.example.mobileapp.data.repository.WorkListRepository

class GetWorkListItemsUseCase (private val workListRepository: WorkListRepository) {
    suspend operator fun invoke(userId: Int): WorkListResult<List<TaskInWorkList>> {
        return workListRepository.getWorkListItems(userId)
    }
}