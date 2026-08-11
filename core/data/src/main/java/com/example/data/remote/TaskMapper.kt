package com.example.data.remote

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.google.firebase.database.DataSnapshot

fun DataSnapshot.toDomainTask(): Task? {
    val dto = getValue(TaskDto::class.java) ?: return null
    val id = key ?: return null
    return Task(
        id = id,
        shortDescription = dto.shortDescription,
        fullDescription = dto.fullDescription,
        status = runCatching { TaskStatus.valueOf(dto.status) }.getOrDefault(TaskStatus.NEW),
        createdAt = dto.createdAt
    )
}