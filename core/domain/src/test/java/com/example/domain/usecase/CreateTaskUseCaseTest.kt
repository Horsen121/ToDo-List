package com.example.domain.usecase

import com.example.domain.fake.FakeTaskRepository
import com.example.domain.model.TaskStatus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CreateTaskUseCaseTest {
    private val repository = FakeTaskRepository()
    private val useCase = CreateTaskUseCase(repository)

    @Test
    fun `task with valid description is created successfully`() = runTest {
        val result = useCase("Написать тесты", "Написать тесты для UseCase Domain")

        assertEquals(TaskActionResult.Success, result)
        val tasks = repository.observeTasks().first()
        assertEquals(1, tasks.size)
        assertEquals(TaskStatus.NEW, tasks.first().status)
    }

    @Test
    fun `task with blank short description is rejected`() = runTest {
        val result = useCase("", "Полное описание")

        assertTrue(result is TaskActionResult.Failure)
        assertTrue(repository.observeTasks().first().isEmpty())
    }

    @Test
    fun `task with blank short description consisting of spaces is rejected`() = runTest {
        val result = useCase("   ", "Полное описание")

        assertTrue(result is TaskActionResult.Failure)
    }
}