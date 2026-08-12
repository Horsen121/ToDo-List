package com.example.taskdetail

import com.example.domain.model.Task

sealed class TaskDetailUiState {
    data object Loading : TaskDetailUiState()
    data class Content(val task: Task) : TaskDetailUiState()
    data object NotFound : TaskDetailUiState()
}