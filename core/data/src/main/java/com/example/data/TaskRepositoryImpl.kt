package com.example.data

import com.example.data.local.TaskLocalDataSource
import com.example.data.remote.TaskRemoteDataSource
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val remoteDataSource: TaskRemoteDataSource,
    private val localDataSource: TaskLocalDataSource,
    @ApplicationScope private val scope: CoroutineScope
) : TaskRepository {

    init {
        scope.launch {
            remoteDataSource.observeRemoteTasks().collect { remoteTasks ->
                localDataSource.replaceAll(remoteTasks)
            }
        }
    }

    override fun observeTasks(): Flow<List<Task>> = localDataSource.observeTasks()
    override fun observeTaskById(taskId: String): Flow<Task?> = localDataSource.observeTaskById(taskId)

    override suspend fun addTask(shortDescription: String, fullDescription: String) =
        remoteDataSource.addTask(shortDescription, fullDescription)

    override suspend fun updateStatus(taskId: String, newStatus: TaskStatus) =
        remoteDataSource.updateStatus(taskId, newStatus)

    override suspend fun deleteTask(taskId: String) =
        remoteDataSource.deleteTask(taskId)
}