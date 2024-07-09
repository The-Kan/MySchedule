package com.my.schedule.ui.api.internal.data.remote

import com.my.schedule.ui.api.internal.data.remote.client.NetworkClient
import com.my.schedule.ui.api.internal.network.ApiTask
import com.my.schedule.ui.api.internal.network.NetworkTask
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.my.schedule.ui.api.internal.data.model.Post
import okhttp3.Request

internal class ServiceApi(
    networkTimeoutMs: Long
) {
    private val networkClient = NetworkClient(networkTimeoutMs)
    private val gson = GsonBuilder().create()

    // 서버 주소
    private val BASE_URL = "https://jsonplaceholder.typicode.com"

    fun post(): ApiTask<List<Post>> {
        return try {
            val request = Request.Builder().run {
                url("$BASE_URL/Posts")
                build()
            }

            val responseType = object : TypeToken<List<Post>>() {}.type
            NetworkTask(networkClient.getClient()).request(request) { response ->
                gson.fromJson(response, responseType)
            }
        } catch (e: Exception) {
            ApiTask.Fail(e)
        }
    }
}
