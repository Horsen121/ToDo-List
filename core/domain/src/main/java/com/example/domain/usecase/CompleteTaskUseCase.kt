package com.example.domain.usecase

import com.example.domain.TaskException
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(task: Task): Result<Unit> {
        if (task.status != TaskStatus.IN_PROGRESS) {
            return Result.failure(
                TaskException.InvalidTaskActionException("Выполнить можно только задачу в работе")
            )
        }

        return repository.updateStatus(task.id, TaskStatus.DONE)
    }
}