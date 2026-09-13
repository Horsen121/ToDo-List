package com.example.data.remote

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