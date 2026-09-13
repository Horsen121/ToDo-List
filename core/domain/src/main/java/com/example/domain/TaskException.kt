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

fun Throwable.toUserMessage(): String =
    when (this) {
        is TaskException.InvalidTaskActionException ->
            message ?: "Действие с задачей недоступно"

        is TaskException.TaskStorageUnavailableException ->
            "Не удалось подключиться к хранилищу задач"

        is TaskException.InvalidTaskDataException ->
            "Получены некорректные данные задачи"

        is TaskException.UnknownTaskException ->
            "Не удалось выполнить операцию"

        else ->
            "Произошла неизвестная ошибка"
    }
