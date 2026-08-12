package com.example.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.ObserveTaskByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val observeTaskById: ObserveTaskByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: String = checkNotNull(savedStateHandle["taskId"])

    val uiState: StateFlow<TaskDetailUiState> = observeTaskById(taskId)
        .map { task -> if (task != null) TaskDetailUiState.Content(task) else TaskDetailUiState.NotFound }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskDetailUiState.Loading
        )
}