package com.example.domain.model

data class Task(
    val id: String = "",
    val shortDescription: String,
    val fullDescription: String,
    val status: TaskStatus,
    val createdAt: Long
)
