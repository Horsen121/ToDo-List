package com.example.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.toUserMessage
import com.example.domain.usecase.CreateTaskUseCase
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

    private val _uiState = MutableStateFlow<AddTaskUiState>(AddTaskUiState.Editing)
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()

    fun onSave(shortDescription: String, fullDescription: String) {
        viewModelScope.launch {
            _uiState.update { AddTaskUiState.Editing }
            createTask(shortDescription, fullDescription).fold(
                onSuccess = {
                    _uiState.value = AddTaskUiState.Saved
                },
                onFailure = { error ->
                    _uiState.value = AddTaskUiState.Error(
                        message = error.toUserMessage()
                    )
                }
            )
        }
    }
}