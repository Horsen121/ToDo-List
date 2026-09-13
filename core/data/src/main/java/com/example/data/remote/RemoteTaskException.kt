package com.example.data.remote

import com.example.domain.TaskException

sealed class RemoteTaskException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
    class InvalidRemoteTaskDataException(
        message: String,
        cause: Throwable? = null
    ) : RemoteTaskException(message, cause)

    class ObserveRemoteTasksException(
        val errorCode: Int,
        message: String,
        cause: Throwable? = null
    ) : RemoteTaskException(message, cause)

    class RemoteTaskWriteException(
        val operation: RemoteTaskOperation,
        cause: Throwable
    ) : RemoteTaskException(
        message = "Remote task operation '$operation' failed",
        cause = cause
    )
}

enum class RemoteTaskOperation {
    CREATE,
    UPDATE_STATUS,
    DELETE
}

internal fun Exception.toTaskException(): TaskException =
    when (this) {
        is TaskException -> this

        is RemoteTaskException.InvalidRemoteTaskDataException ->
            TaskException.InvalidTaskDataException(this)

        is RemoteTaskException.RemoteTaskWriteException ->
            TaskException.TaskStorageUnavailableException(this)

        else ->
            TaskException.UnknownTaskException(this)
    }