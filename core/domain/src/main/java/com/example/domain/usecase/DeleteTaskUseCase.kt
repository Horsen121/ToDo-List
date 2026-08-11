package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task): TaskActionResult {
        if (task.status == TaskStatus.IN_PROGRESS || task.status == TaskStatus.DONE) {
            return TaskActionResult.Failure("Удалить можно только новую задачу")
        }
        repository.deleteTask(task.id)
        return TaskActionResult.Success
    }
}