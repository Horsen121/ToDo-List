package com.example.data.local

class InvalidLocalTaskDataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)