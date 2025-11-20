package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.WorkListResult
import com.example.mobileapp.data.repository.WorkListRepository

class ClearWorkListUseCase(private val workListRepository: WorkListRepository) {
    suspend operator fun invoke(userId: Int): WorkListResult<Unit> {
        return workListRepository.clearWorkList(userId)
    }
}