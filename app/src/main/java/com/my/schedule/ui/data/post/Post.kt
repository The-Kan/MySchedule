package com.my.schedule.ui.data.post

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

data class Post (
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

interface ApiService {
    @GET("/posts")
    fun getPosts(): Single<List<Post>>
}