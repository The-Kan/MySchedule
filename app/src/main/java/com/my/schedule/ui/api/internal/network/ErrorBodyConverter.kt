package com.my.schedule.ui.api.internal.network

import com.google.gson.Gson

internal class ErrorBodyConverter {

    companion object {
        @JvmStatic
        fun <T> convert(classOfT: Class<T>, errorBody: String?): T? {
            if (errorBody.isNullOrEmpty()) {
                return null
            }

            return try {
                Gson().fromJson(errorBody, classOfT)
            } catch (e: Exception) {
                null
            }
        }
    }
}