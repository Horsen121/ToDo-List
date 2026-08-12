package com.example.data.local

import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskLocalDataSource {
    fun observeTasks(): Flow<List<Task>>
    fun observeTaskById(taskId: String): Flow<Task?>
    suspend fun replaceAll(tasks: List<Task>)
}