package com.example.domain.usecase

import com.example.domain.repository.TaskRepository

class CreateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(shortDescription: String, fullDescription: String): TaskActionResult {
        if (shortDescription.isBlank()) {
            return TaskActionResult.Failure("Краткое описание не может быть пустым")
        }
        repository.addTask(shortDescription, fullDescription)
        return TaskActionResult.Success
    }
}