package com.example.domain.usecase

import com.example.domain.TaskException
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(task: Task): Result<Unit> {
        if (task.status != TaskStatus.NEW) {
            return Result.failure(
                TaskException.InvalidTaskActionException("Удалить можно только новую задачу")
            )
        }

        return repository.deleteTask(task.id)
    }
}