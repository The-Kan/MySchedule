package com.my.schedule.ui.api.internal.data.remote.client

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

internal class NetworkClient(
    networkTimeoutMs: Long
) {
    // okHttp Client 생성
    private var client = OkHttpClient.Builder()
        .addUnSavedCookieStore()
        .readTimeout(networkTimeoutMs, TimeUnit.MILLISECONDS)
        .connectTimeout(networkTimeoutMs, TimeUnit.MILLISECONDS)
        .build()

    fun getClient(): OkHttpClient = client

    private fun OkHttpClient.Builder.addUnSavedCookieStore() = apply {
        cookieJar(object : CookieJar {
            private val cookieStore = HashMap<String, List<Cookie>>()
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host()] = cookies
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return cookieStore[url.host()] ?: ArrayList()
            }
        })
    }
}