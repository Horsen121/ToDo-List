package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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

fun TaskEntity.toDomain() = Task(
    id,
    shortDescription,
    fullDescription,
    TaskStatus.valueOf(status),
    createdAt
)

fun Task.toEntity() = TaskEntity(
    id,
    shortDescription,
    fullDescription,
    status.name,
    createdAt
)