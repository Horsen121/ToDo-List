package com.example.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.usecase.TaskActionResult
import com.example.domain.usecase.TaskUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskListViewModelFactory(
    private val useCases: TaskUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TaskListViewModel(useCases) as T
    }
}

class TaskListViewModel(
    private val useCases: TaskUseCases
) : ViewModel() {

    private val _events = Channel<TaskListEvent>(Channel.BUFFERED)
    val events: Flow<TaskListEvent> = _events.receiveAsFlow()

    val uiState: StateFlow<TaskListUiState> = useCases.observeTasks()
        .map { tasks ->
            try{
                TaskListUiState.Success(tasks = tasks)
            } catch (e: Throwable) {
                TaskListUiState.Error(e.message ?: "")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskListUiState.Loading
        )

    fun onTakeInProgress(task: Task) = runAction { useCases.takeInProgress(task) }
    fun onComplete(task: Task) = runAction { useCases.complete(task) }
    fun onDelete(task: Task) = runAction { useCases.delete(task) }

    private fun runAction(block: suspend () -> TaskActionResult) {
        viewModelScope.launch {
            when (val result = block()) {
                is TaskActionResult.Failure -> _events.send(TaskListEvent.ShowError(result.reason))
                TaskActionResult.Success -> Unit
            }
        }
    }
}