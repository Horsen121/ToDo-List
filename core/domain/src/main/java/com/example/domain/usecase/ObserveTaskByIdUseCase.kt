package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    operator fun invoke(taskId: String): Flow<Task?> = repository.observeTaskById(taskId)
}