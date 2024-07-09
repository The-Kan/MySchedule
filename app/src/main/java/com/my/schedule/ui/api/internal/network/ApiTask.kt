package com.my.schedule.ui.api.internal.network

internal sealed class ApiTask<T> {
    val isSuccess: Boolean
        get() = when (this) {
            is Success -> true
            else -> false
        }

    val isFailure: Boolean
        get() = !isSuccess


    fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> (this as Success).body
        }

    class Success<T>(val body: T) : ApiTask<T>()

    class ErrorBody<T>(
        val code: Int,
        val message: String,
        val errorBody: String
    ) : ApiTask<T>() {
        inline fun <reified E> toErrorEntity(): E? {
            return ErrorBodyConverter.convert(E::class.java, errorBody)
        }
    }

    class Fail<T>(
        val exception: Exception
    ) : ApiTask<T>()
}