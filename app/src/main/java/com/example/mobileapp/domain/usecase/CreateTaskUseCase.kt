package com.example.mobileapp.domain.usecase


import com.example.mobileapp.data.repository.TaskRepository


class CreateTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(carId: Int, job: String, comment: String?): Boolean {
        return repository.createTask(carId, job, comment)
    }
}