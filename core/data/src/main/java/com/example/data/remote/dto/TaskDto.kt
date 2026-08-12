package com.example.data.remote.dto

import com.example.domain.model.TaskStatus

data class TaskDto(
    val shortDescription: String = "",
    val fullDescription: String = "",
    val status: String = TaskStatus.NEW.name,
    val createdAt: Long = 0L
)