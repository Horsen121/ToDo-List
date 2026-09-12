package com.example.data.remote.mapper

import com.example.data.remote.RemoteTaskException
import com.example.data.remote.dto.TaskDto
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus

internal fun TaskDto.toDomain(taskId: String): Task {
    if (taskId.isBlank()) {
        throw RemoteTaskException.InvalidRemoteTaskDataException("Remote task has an empty id")
    }

    val shortDescription = shortDescription
        ?: throw IllegalStateException("Missing shortDescription for task '$taskId'")

    val fullDescription = fullDescription
        ?: throw IllegalStateException("Missing fullDescription for task '$taskId'")

    val rawStatus = status
        ?: throw IllegalStateException("Missing status for task '$taskId'")

    val createdAt = createdAt
        ?: throw IllegalStateException( "Missing createdAt for task '$taskId'")

    val status = TaskStatus.entries.firstOrNull { taskStatus ->
        taskStatus.name == rawStatus
    } ?: throw IllegalStateException("Unknown status '$rawStatus' for task '$taskId'")

    return Task(
        id = taskId,
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        status = status,
        createdAt = createdAt
    )
}