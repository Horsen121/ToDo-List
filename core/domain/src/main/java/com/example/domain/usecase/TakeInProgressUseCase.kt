package com.example.domain.usecase

import com.example.domain.TaskException
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository
import javax.inject.Inject

class TakeInProgressUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(task: Task): Result<Unit> {
        if (task.status != TaskStatus.NEW) {
            return Result.failure(
                TaskException.InvalidTaskActionException("Взять в работу можно только новую задачу")
            )
        }

        return repository.updateStatus(task.id, TaskStatus.IN_PROGRESS)
    }
}