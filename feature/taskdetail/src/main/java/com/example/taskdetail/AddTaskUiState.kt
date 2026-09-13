package com.example.taskdetail

sealed class AddTaskUiState {
    data object Editing : AddTaskUiState()
    data object Saved : AddTaskUiState()
    data class Error(val message: String): AddTaskUiState()
}