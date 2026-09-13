package com.example.data

import android.util.Log
import com.example.data.local.TaskLocalDataSource
import com.example.data.remote.RemoteTaskException.*
import com.example.data.remote.TaskRemoteDataSource
import com.example.data.remote.mapper.toTaskException
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TaskRepositoryImpl @Inject constructor(
    private val remoteDataSource: TaskRemoteDataSource,
    private val localDataSource: TaskLocalDataSource,
    @param:ApplicationScope private val scope: CoroutineScope
) : TaskRepository {

    private companion object {
        const val TAG = "TaskRepository"
        const val MAX_RETRY_ATTEMPT = 4
    }

    override fun observeTasks(): Flow<List<Task>> = localDataSource.observeTasks()
    override fun observeTaskById(taskId: String): Flow<Task?> = localDataSource.observeTaskById(taskId)

    override suspend fun addTask(shortDescription: String, fullDescription: String): Result<Unit> =
        executeWrite {
            remoteDataSource.addTask(shortDescription, fullDescription)
        }

    override suspend fun updateStatus(taskId: String, newStatus: TaskStatus): Result<Unit> =
        executeWrite {
            remoteDataSource.updateStatus(taskId, newStatus)
        }

    override suspend fun deleteTask(taskId: String): Result<Unit> =
        executeWrite {
            remoteDataSource.deleteTask(taskId)
        }


    init {
        scope.launch {
            runSynchronizationLoop()
        }
    }

    private suspend fun runSynchronizationLoop() {
        var retryAttempt = 0

        while (true) {
            try {
                remoteDataSource.observeRemoteTasks()
                    .collect { remoteTasks ->
                        localDataSource.replaceCache(remoteTasks)
                        retryAttempt = 0
                    }

                retryAttempt = 0
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: InvalidRemoteTaskDataException) {
                Log.e(
                    TAG,
                    "Stopping synchronization due to invalid remote task data",
                    exception
                )
                return
            } catch (exception: ObserveRemoteTasksException) {
                Log.e(
                    TAG,
                    "Remote observation failed with code=${exception.errorCode}: ${exception.message}",
                    exception
                )
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Task synchronization failed",
                    exception
                )
            }

            delay(retryDelay(retryAttempt))
            retryAttempt++
        }
    }

    private fun retryDelay(attempt: Int): Duration =
        when (attempt.coerceAtMost(MAX_RETRY_ATTEMPT)) {
            0 -> 1.seconds
            1 -> 2.seconds
            2 -> 4.seconds
            3 -> 8.seconds
            else -> 16.seconds
        }

    private suspend fun executeWrite(block: suspend () -> Unit): Result<Unit> =
        try {
            block()
            Result.success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception.toTaskException())
        }
}