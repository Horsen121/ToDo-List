package com.example.ui

import androidx.annotation.StringRes

sealed interface UiText {

    data class Dynamic(
        val value: String
    ) : UiText

    data class Resource(
        @StringRes val resourceId: Int
    ) : UiText
}