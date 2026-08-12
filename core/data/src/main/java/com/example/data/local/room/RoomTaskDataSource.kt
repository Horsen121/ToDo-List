package com.example.data.local.room

import com.example.data.local.TaskLocalDataSource
import com.example.data.local.dao.TaskDao
import com.example.data.local.entity.toDomain
import com.example.data.local.entity.toEntity
import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class RoomTaskDataSource @Inject constructor(
    private val taskDao: TaskDao
) : TaskLocalDataSource {

    override fun observeTasks(): Flow<List<Task>> =
        taskDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeTaskById(taskId: String): Flow<Task?> =
        taskDao.observeById(taskId).map { it?.toDomain() }

    override suspend fun replaceAll(tasks: List<Task>) {
        taskDao.clearAll()
        taskDao.upsertAll(tasks.map { it.toEntity() })
    }
}