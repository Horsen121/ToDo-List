package com.example.domain.usecase

import com.example.domain.fake.FakeTaskRepository
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CompleteTaskUseCaseTest {
    private val repository = FakeTaskRepository()
    private val useCase = CompleteTaskUseCase(repository)

    @Test
    fun `task not in progress cannot be completed`() = runTest {
        val newTask = Task(
            "id1",
            "Написать тесты",
            "Написать тесты для UseCase Domain",
            TaskStatus.NEW,
            0L
        )

        val result = useCase(newTask)

        assertTrue(result is TaskActionResult.Failure)
    }

    @Test
    fun `task in progress can be completed`() = runTest {
        val inProgress = Task(
            "id1",
            "Написать тесты",
            "Написать тесты для UseCase Domain",
            TaskStatus.IN_PROGRESS,
            0L
        )

        val result = useCase(inProgress)

        assertEquals(TaskActionResult.Success, result)
    }
}