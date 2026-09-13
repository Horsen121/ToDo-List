package com.example.tasklist

import com.example.domain.model.Task
import com.example.ui.UiText

sealed class TaskListUiState {
    data object Loading: TaskListUiState()
    data class Success(val tasks: List<Task>): TaskListUiState()
    data class Error(val message: String): TaskListUiState()
}

sealed interface TaskListEvent {
    data class ShowError(val message: UiText) : TaskListEvent
}