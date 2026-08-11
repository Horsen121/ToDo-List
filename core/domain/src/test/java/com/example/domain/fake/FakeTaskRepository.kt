package com.example.domain.fake

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeTaskRepository: TaskRepository {
    private val state = MutableStateFlow<List<Task>>(emptyList())
    private var idCounter = 0

    override fun observeTasks(): Flow<List<Task>> = state.asStateFlow()

    override fun observeTaskById(taskId: String): Flow<Task?> =
        state.map { tasks -> tasks.find { it.id == taskId } }

    override suspend fun addTask(shortDescription: String, fullDescription: String) {
        val task = Task(
            id = "task_${idCounter++}",
            shortDescription = shortDescription,
            fullDescription = fullDescription,
            status = TaskStatus.NEW,
            createdAt = 0L
        )
        state.value += task
    }

    override suspend fun updateStatus(taskId: String, newStatus: TaskStatus) {
        state.value = state.value.map { if (it.id == taskId) it.copy(status = newStatus) else it }
    }

    override suspend fun deleteTask(taskId: String) {
        state.value = state.value.filterNot { it.id == taskId }
    }
}