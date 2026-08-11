package com.example.domain.usecase

import com.example.domain.fake.FakeTaskRepository
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteTaskUseCaseTest {
    private val repository = FakeTaskRepository()
    private val useCase = DeleteTaskUseCase(repository)

    @Test
    fun `in progress task cannot be deleted`() = runTest {
        val inProgress = Task(
            "id1",
            "Написать тесты",
            "Написать тесты для UseCase Domain",
            TaskStatus.IN_PROGRESS,
            0L
        )

        val result = useCase(inProgress)

        assertTrue(result is TaskActionResult.Failure)
    }

    @Test
    fun `done task cannot be deleted`() = runTest {
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

    @Test
    fun `new task can be deleted`() = runTest {
        repository.addTask("Удалить", "Полное описание")
        val task = repository.observeTasks().first().first()

        val result = useCase(task)

        assertEquals(TaskActionResult.Success, result)
        assertTrue(repository.observeTasks().first().isEmpty())
    }
}