package com.example.taskdetail

sealed class AddTaskUiState {
    data object Saving : AddTaskUiState()
    data object Saved : AddTaskUiState()
    data class Error(val error: String): AddTaskUiState()
}