package com.example.domain

sealed class TaskException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    class InvalidTaskActionException(
        message: String
    ) : TaskException(message)

    class TaskStorageUnavailableException(
        cause: Throwable
    ) : TaskException(
        message = "Task storage is unavailable",
        cause = cause
    )

    class InvalidTaskDataException(
        cause: Throwable
    ) : TaskException(
        message = "Task data is invalid",
        cause = cause
    )

    class UnknownTaskException(
        cause: Throwable
    ) : TaskException(
        message = "Unknown task error",
        cause = cause
    )
}
