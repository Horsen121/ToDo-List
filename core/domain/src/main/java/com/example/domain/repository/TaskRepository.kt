package com.example.domain.repository

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun observeTasks(): Flow<List<Task>>
    fun observeTaskById(taskId: String): Flow<Task?>

    suspend fun addTask(shortDescription: String, fullDescription: String): Result<Unit>

    suspend fun updateStatus(taskId: String, newStatus: TaskStatus): Result<Unit>

    suspend fun deleteTask(taskId: String): Result<Unit>
}