package com.example.data.remote.firebase

import com.example.data.remote.RemoteTaskException.InvalidRemoteTaskDataException
import com.example.data.remote.RemoteTaskException.ObserveRemoteTasksException
import com.example.data.remote.RemoteTaskException.RemoteTaskWriteException
import com.example.data.remote.RemoteTaskOperation
import com.example.data.remote.TaskRemoteDataSource
import com.example.data.remote.TasksReference
import com.example.data.remote.dto.TaskDto
import com.example.data.remote.mapper.toDomain
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTaskDataSource @Inject constructor(
    @param:TasksReference
    private val tasksRef: DatabaseReference
) : TaskRemoteDataSource {

    override fun observeRemoteTasks(): Flow<List<Task>> =
        callbackFlow {
            val listener = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val tasks = snapshot.children.map { child ->
                            child.toDomainTask()
                        }

                        trySend(tasks).onFailure { cause ->
                            if (cause != null) {
                                close(cause)
                            }
                        }
                    } catch (exception: Exception) {
                        close(exception)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    close(
                        ObserveRemoteTasksException(
                            errorCode = error.code,
                            message = error.message,
                            cause = error.toException()
                        )
                    )
                }
            }

            tasksRef.addValueEventListener(listener)

            awaitClose {
                tasksRef.removeEventListener(listener)
            }
        }.buffer(Channel.CONFLATED)

    override suspend fun addTask(
        shortDescription: String,
        fullDescription: String
    ) {
        val taskId = tasksRef.push().key
            ?: throw RemoteTaskWriteException(
                operation = RemoteTaskOperation.CREATE,
                cause = IllegalStateException("Firebase did not generate a task id")
            )

        val values = mapOf(
            FIELD_SHORT_DESCRIPTION to shortDescription,
            FIELD_FULL_DESCRIPTION to fullDescription,
            FIELD_STATUS to TaskStatus.NEW.name,
            FIELD_CREATED_AT to ServerValue.TIMESTAMP
        )

        executeWrite(RemoteTaskOperation.CREATE) {
            tasksRef.child(taskId)
                .setValue(values)
                .await()
        }
    }

    override suspend fun updateStatus(
        taskId: String,
        newStatus: TaskStatus
    ) {
        val taskReference = taskReference(taskId)

        executeWrite(RemoteTaskOperation.UPDATE_STATUS) {
            taskReference.child(FIELD_STATUS)
                .setValue(newStatus.name)
                .await()
        }
    }

    override suspend fun deleteTask(taskId: String) {
        val taskReference = taskReference(taskId)

        executeWrite(RemoteTaskOperation.DELETE) {
            taskReference
                .removeValue()
                .await()
        }
    }

    private fun DataSnapshot.toDomainTask(): Task {
        val taskId = key
            ?: throw InvalidRemoteTaskDataException("Firebase task has no id")

        val dto = try {
            getValue(TaskDto::class.java)
        } catch (exception: Exception) {
            throw InvalidRemoteTaskDataException(
                message = "Cannot deserialize task '$taskId'",
                cause = exception
            )
        } ?: throw InvalidRemoteTaskDataException("Task '$taskId' has no data")

        return dto.toDomain(taskId)
    }

    private fun taskReference(taskId: String): DatabaseReference {
        require(taskId.isValidFirebaseKey()) {
            "Invalid Firebase task id: '$taskId'"
        }

        return tasksRef.child(taskId)
    }

    private suspend fun executeWrite(
        operation: RemoteTaskOperation,
        block: suspend () -> Unit
    ) {
        try {
            block()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            throw RemoteTaskWriteException(
                operation = operation,
                cause = exception
            )
        }
    }

    private fun String.isValidFirebaseKey(): Boolean =
        isNotBlank() && none { character ->
            character == '.' ||
                    character == '#' ||
                    character == '$' ||
                    character == '[' ||
                    character == ']' ||
                    character == '/' ||
                    character.code in 0..31 ||
                    character.code == 127
        }

    private companion object {
        const val FIELD_SHORT_DESCRIPTION = "shortDescription"
        const val FIELD_FULL_DESCRIPTION = "fullDescription"
        const val FIELD_STATUS = "status"
        const val FIELD_CREATED_AT = "createdAt"
    }
}