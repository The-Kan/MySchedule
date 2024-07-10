package com.my.schedule.ui.api.model

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
) {
    sealed interface State
    data class ConnectFailed(val message: String) : State
    data class Success(val result: List<Post>) : State
    data class ServerError(val code: String, val message: String) : State
}