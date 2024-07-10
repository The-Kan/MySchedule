package com.my.schedule.ui.api.internal.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

internal class NetworkTask(
    private val client: OkHttpClient
) {
    fun <T> request(request: Request, converter: (String) -> T): ApiTask<T> {
        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                parseError(response)
            } else {
                response.body()?.string()?.let {
                    ApiTask.Success(converter(it))
                } ?: ApiTask.Fail(IllegalStateException("Empty Body"))
            }
        } catch (e: Exception) {
            ApiTask.Fail(e)
        }
    }

    private fun <T> parseError(response: Response): ApiTask<T> {
        val errorBody = response.body()?.string().orEmpty()
        return ApiTask.ErrorBody(response.code(), response.message(), errorBody)
    }
}
