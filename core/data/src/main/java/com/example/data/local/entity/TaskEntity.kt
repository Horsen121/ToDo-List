package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.local.InvalidLocalTaskDataException
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val shortDescription: String,
    val fullDescription: String,
    val status: String,
    val createdAt: Long
)

internal fun TaskEntity.toDomain(): Task {
    val taskStatus = TaskStatus.entries.firstOrNull { taskStatus ->
        taskStatus.name == status
    } ?: throw InvalidLocalTaskDataException("Unknown local status '$status' for task '$id'")

    return Task(
        id = id,
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        status = taskStatus,
        createdAt = createdAt
    )
}

internal fun Task.toEntity(): TaskEntity =
    TaskEntity(
        id = id,
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        status = status.name,
        createdAt = createdAt
    )