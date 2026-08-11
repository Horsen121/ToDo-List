package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository

class TakeInProgressUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task): TaskActionResult {
        if (task.status != TaskStatus.NEW) {
            return TaskActionResult.Failure("Взять в работу можно только новую задачу")
        }
        repository.updateStatus(task.id, TaskStatus.IN_PROGRESS)
        return TaskActionResult.Success
    }
}