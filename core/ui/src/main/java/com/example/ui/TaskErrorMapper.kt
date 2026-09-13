package com.example.ui

import android.content.Context
import com.example.core.ui.R
import com.example.domain.TaskException

fun Throwable.toUiText(): UiText =
    when (this) {
        is TaskException.InvalidTaskActionException ->
            message
                ?.takeIf(String::isNotBlank)
                ?.let(UiText::Dynamic)
                ?: UiText.Resource(R.string.error_task_action_unavailable)

        is TaskException.TaskStorageUnavailableException ->
            UiText.Resource(R.string.error_task_storage_unavailable)

        is TaskException.InvalidTaskDataException ->
            UiText.Resource(R.string.error_invalid_task_data)

        is TaskException.UnknownTaskException ->
            UiText.Resource(R.string.error_unknown_task)

        else ->
            UiText.Resource(R.string.error_unknown)
    }

fun UiText.asString(context: Context): String =
    when (this) {
        is UiText.Dynamic ->
            value

        is UiText.Resource ->
            context.getString(resourceId)
    }