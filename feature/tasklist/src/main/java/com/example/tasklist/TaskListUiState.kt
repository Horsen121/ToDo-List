package com.example.tasklist

import com.example.domain.model.Task

sealed class TaskListUiState {
    data object Loading: TaskListUiState()
    data class Success(val tasks: List<Task>): TaskListUiState()
    data class Error(val error: String): TaskListUiState()
}

sealed interface TaskListEvent {
    data class ShowError(val message: String) : TaskListEvent
}