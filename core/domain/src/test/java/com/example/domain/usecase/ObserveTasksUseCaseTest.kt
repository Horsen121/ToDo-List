package com.example.domain.usecase

import app.cash.turbine.test
import com.example.domain.fake.FakeTaskRepository
import com.example.domain.model.Task
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveTasksUseCaseTest {
    private val repository = FakeTaskRepository()
    private val useCase = ObserveTasksUseCase(repository)

    @Test
    fun `emits updated list after adding a task`() = runTest {
        useCase().test {
            assertEquals(emptyList<Task>(), awaitItem())
            repository.addTask("Новая задача", "Описание")
            assertEquals(1, awaitItem().size)
        }
    }
}