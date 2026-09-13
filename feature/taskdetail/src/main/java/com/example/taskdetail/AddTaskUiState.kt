package com.example.taskdetail

import com.example.ui.UiText

sealed class AddTaskUiState {
    data object Editing : AddTaskUiState()
    data object Saving : AddTaskUiState()
    data class Error(val message: UiText): AddTaskUiState()
}