package com.example.mobileapp.domain.usecase

import com.example.mobileapp.data.model.WorkListResult
import com.example.mobileapp.data.repository.WorkListRepository

class AddToWorkListUseCase(private val workListRepository: WorkListRepository) {
    suspend operator fun invoke(userId: Int, productId: Int): WorkListResult<Unit> {
        return workListRepository.addToWorkList(userId, productId)
    }
}