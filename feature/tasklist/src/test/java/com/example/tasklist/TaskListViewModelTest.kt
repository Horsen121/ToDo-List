package com.example.tasklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.domain.fake.FakeTaskRepository
import com.example.domain.fake.MainDispatcherRule
import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.CompleteTaskUseCase
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.ObserveTaskByIdUseCase
import com.example.domain.usecase.ObserveTasksUseCase
import com.example.domain.usecase.TakeInProgressUseCase
import com.example.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


class TaskListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `uiState reflects tasks from repository`() = runTest {
        val repository = FakeTaskRepository()
        val viewModel = TaskListViewModel(
            observeTasks = ObserveTasksUseCase(repository),
            takeInProgress = TakeInProgressUseCase(repository),
            complete = CompleteTaskUseCase(repository),
            delete = DeleteTaskUseCase(repository)
        )

        repository.addTask("Тест", "Описание")

        viewModel.uiState.test {
            val state = awaitItem() as TaskListUiState.Success
            assertEquals(1, state.tasks.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDelete for in-progress task emits error event`() = runTest {
        val repository = FakeTaskRepository()
        val viewModel = TaskListViewModel(
            observeTasks = ObserveTasksUseCase(repository),
            takeInProgress = TakeInProgressUseCase(repository),
            complete = CompleteTaskUseCase(repository),
            delete = DeleteTaskUseCase(repository)
        )
        repository.addTask("Тест", "Описание")
        val task = repository.observeTasks().first().first()
        viewModel.onTakeInProgress(task)
        val inProgressTask = repository.observeTasks().first().first()

        viewModel.events.test {
            viewModel.onDelete(inProgressTask)
            val event = awaitItem()
            assertTrue(event is TaskListEvent.ShowError)
        }
    }
}