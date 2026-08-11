package com.example.domain.usecase

sealed class TaskActionResult {
    data object Success : TaskActionResult()
    data class Failure(val reason: String) : TaskActionResult()
}