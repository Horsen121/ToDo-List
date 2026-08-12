package com.example.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TaskDetailViewModelFactory(
    private val useCases: TaskUseCases,
    private val taskId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TaskDetailViewModel(useCases, taskId) as T
    }
}

class TaskDetailViewModel(
    useCases: TaskUseCases,
    taskId: String
) : ViewModel() {

    val uiState: StateFlow<TaskDetailUiState> = useCases.observeTaskById(taskId)
        .map { task -> if (task != null) TaskDetailUiState.Content(task) else TaskDetailUiState.NotFound }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskDetailUiState.Loading
        )
}