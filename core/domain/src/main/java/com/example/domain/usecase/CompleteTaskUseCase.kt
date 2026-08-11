package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository

class CompleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task): TaskActionResult {
        if (task.status != TaskStatus.IN_PROGRESS) {
            return TaskActionResult.Failure("Выполнить можно только задачу в работе")
        }
        repository.updateStatus(task.id, TaskStatus.DONE)
        return TaskActionResult.Success
    }
}