package com.example.domain.usecase

import com.example.domain.TaskException
import com.example.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(shortDescription: String, fullDescription: String): Result<Unit> {
        if (shortDescription.isBlank()) {
            return Result.failure(TaskException.InvalidTaskActionException("Краткое описание не может быть пустым"))
        }
        return repository.addTask(shortDescription, fullDescription)
    }
}