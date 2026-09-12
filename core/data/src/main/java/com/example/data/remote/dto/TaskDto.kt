package com.example.data.remote.dto

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TaskDto(
    val shortDescription: String? = null,
    val fullDescription: String? = null,
    val status: String? = null,
    val createdAt: Long? = null
)