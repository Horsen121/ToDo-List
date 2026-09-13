package com.example.data.remote.mapper

import com.example.data.remote.RemoteTaskException
import com.example.domain.TaskException

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