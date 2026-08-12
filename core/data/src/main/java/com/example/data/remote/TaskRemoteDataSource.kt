package com.example.data.remote

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRemoteDataSource {
    fun observeRemoteTasks(): Flow<List<Task>>
    suspend fun addTask(shortDescription: String, fullDescription: String)
    suspend fun updateStatus(taskId: String, newStatus: TaskStatus)
    suspend fun deleteTask(taskId: String)
}