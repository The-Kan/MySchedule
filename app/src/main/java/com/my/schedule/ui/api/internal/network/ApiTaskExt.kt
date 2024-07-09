package com.my.schedule.ui.api.internal.network

internal fun <T, R> ApiTask<T>.map(onSuccess: (ApiTask.Success<T>) -> ApiTask<R>): ApiTask<R> {
    return when (this) {
        is ApiTask.Success -> onSuccess(this)
        is ApiTask.ErrorBody -> ApiTask.ErrorBody(code, message, errorBody)
        is ApiTask.Fail -> ApiTask.Fail(exception)
    }
}