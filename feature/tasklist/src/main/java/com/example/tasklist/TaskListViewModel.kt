package com.example.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.usecase.CompleteTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.ObserveTasksUseCase
import com.example.domain.usecase.TakeInProgressUseCase
import com.example.domain.usecase.TaskActionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val observeTasks: ObserveTasksUseCase,
    private val takeInProgress: TakeInProgressUseCase,
    private val complete: CompleteTaskUseCase,
    private val delete: DeleteTaskUseCase
) : ViewModel() {

    private val _events = Channel<TaskListEvent>(Channel.BUFFERED)
    val events: Flow<TaskListEvent> = _events.receiveAsFlow()

    val uiState: StateFlow<TaskListUiState> = observeTasks()
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

    fun onTakeInProgress(task: Task) = runAction { takeInProgress(task) }
    fun onComplete(task: Task) = runAction { complete(task) }
    fun onDelete(task: Task) = runAction { delete(task) }

    private fun runAction(block: suspend () -> TaskActionResult) {
        viewModelScope.launch {
            when (val result = block()) {
                is TaskActionResult.Failure -> _events.send(TaskListEvent.ShowError(result.reason))
                TaskActionResult.Success -> Unit
            }
        }
    }
}