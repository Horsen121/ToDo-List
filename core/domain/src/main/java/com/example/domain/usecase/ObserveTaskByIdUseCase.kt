package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveTaskByIdUseCase(private val repository: TaskRepository) {
    operator fun invoke(taskId: String): Flow<Task?> = repository.observeTaskById(taskId)
}