package com.example.domain.usecase

import com.example.domain.fake.FakeTaskRepository
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TakeInProgressUseCaseTest {
    private val repository = FakeTaskRepository()
    private val useCase = TakeInProgressUseCase(repository)

    @Test
    fun `new task can be taken into progress`() = runTest {
        repository.addTask("Написать тесты", "Написать тесты для UseCase Domain")
        val task = repository.observeTasks().first().first()

        val result = useCase(task)

        assertEquals(TaskActionResult.Success, result)
        val updated = repository.observeTasks().first().first()
        assertEquals(TaskStatus.IN_PROGRESS, updated.status)
    }

    @Test
    fun `task already in progress cannot be taken again`() = runTest {
        repository.addTask("Написать тесты", "Написать тесты для UseCase Domain")
        var task = repository.observeTasks().first().first()
        useCase(task)
        task = repository.observeTasks().first().first()

        val result = useCase(task)

        assertTrue(result is TaskActionResult.Failure)
    }

    @Test
    fun `done task cannot be taken into progress`() = runTest {
        val done = Task(
            "id1",
            "Написать тесты",
            "Написать тесты для UseCase Domain",
            TaskStatus.DONE,
            0L
        )

        val result = useCase(done)

        assertTrue(result is TaskActionResult.Failure)
    }
}