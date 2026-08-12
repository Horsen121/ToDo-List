package com.example.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.TaskActionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val createTask: CreateTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddTaskUiState>(AddTaskUiState.Saving)
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()

    fun onSave(shortDescription: String, fullDescription: String) {
        viewModelScope.launch {
            _uiState.update { AddTaskUiState.Saving }
            when (val result = createTask(shortDescription, fullDescription)) {
                is TaskActionResult.Failure -> _uiState.update {
                    AddTaskUiState.Error(result.reason)
                }
                TaskActionResult.Success -> _uiState.update {
                    AddTaskUiState.Saved
                }
            }
        }
    }
}