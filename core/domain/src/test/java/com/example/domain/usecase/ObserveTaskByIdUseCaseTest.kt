package com.example.domain.usecase

import app.cash.turbine.test
import com.example.domain.fake.FakeTaskRepository
import com.example.domain.model.TaskStatus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveTaskByIdUseCaseTest {
    private val repository = FakeTaskRepository()
    private val useCase = ObserveTaskByIdUseCase(repository)

    @Test
    fun `returns null when task does not exist`() = runTest {
        useCase("unknown_id").test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `emits task when it exists and updates on status change`() = runTest {
        repository.addTask("Написать тесты", "Написать тесты для UseCase Domain")
        val id = repository.observeTasks().first().first().id

        useCase(id).test {
            assertEquals(TaskStatus.NEW, awaitItem()?.status)
            repository.updateStatus(id, TaskStatus.IN_PROGRESS)
            assertEquals(TaskStatus.IN_PROGRESS, awaitItem()?.status)
        }
    }

    @Test
    fun `emits null after task is deleted`() = runTest {
        repository.addTask("Написать тесты", "Написать тесты для UseCase Domain")
        val id = repository.observeTasks().first().first().id

        useCase(id).test {
            assertNotNull(awaitItem())
            repository.deleteTask(id)
            assertNull(awaitItem())
        }
    }
}